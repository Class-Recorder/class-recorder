package com.classrecorder.teacherserver.modules.ffmpegwrapper;

import com.classrecorder.teacherserver.modules.ffmpegwrapper.exceptions.*;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.formats.FfmpegContainerFormat;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.formats.FfmpegFormat;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.video.Cut;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.video.VideoCutInfo;
import com.classrecorder.teacherserver.util.IsoToUtf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

public class ICommandWindows implements ICommand {

    private static final Logger log = LoggerFactory.getLogger(ICommandWindows.class);
    private String defaultAudioDevice;
    private String ffmpegDirectory;

    public ICommandWindows(String ffmpegDirectory) throws IOException {
        this.ffmpegDirectory = ffmpegDirectory;
        log.info(this.ffmpegDirectory);
        this.defaultAudioDevice = extractDefaultAudioDevice();
    }

    @Override
    public Process executeFfmpegVideoAndSound(int screenWidth, int screenHeight, int frameRate, String directory, String name, FfmpegContainerFormat cFormat) throws IOException, ICommandException {
        checkDirectory(directory);
        checkFile(name, cFormat, directory, false);

        List<String> command = new ArrayList<>();
        command.addAll(Arrays.asList(this.ffmpegDirectory, "-f", "gdigrab", "-framerate", Integer.toString(frameRate)));
        command.addAll(Arrays.asList("-s", screenWidth + "x" + screenHeight, "-i", "desktop"));
        if(cFormat.equals(FfmpegContainerFormat.mkv))  {
            command.addAll(Arrays.asList("-vcodec", "h264", "-thread_queue_size", "20480", "-f", "dshow", "-i", "audio=" + this.defaultAudioDevice, "-pix_fmt", "yuv420p"));
            command.addAll(Arrays.asList("-acodec", "mp3", "-preset", "ultrafast", "-crf", "30"));
        }
        if(cFormat.equals(FfmpegContainerFormat.mp4)) {
            command.addAll(Arrays.asList("-vcodec", "h264", "-thread_queue_size", "20480", "-f", "dshow", "-i", "audio=" + this.defaultAudioDevice, "-pix_fmt", "yuv420p"));
            command.addAll(Arrays.asList("-acodec", "aac", "-strict", "-2", "-preset", "ultrafast", "-crf", "30"));
        }
        command.addAll(Arrays.asList(directory + "/" + name + "." + cFormat));
        command = IsoToUtf.windowsConvert(command);
        logCommand(command);
        ProcessBuilder pb = new ProcessBuilder(command);

        return pb.start();
    }

    @Override
    public Process executeFfmpegMergeVideoAudio(String dirAudioToMerge, String dirVideoToMerge, String directory, String newVideo, FfmpegContainerFormat cFormatNewVideo) throws IOException, ICommandException {
        checkDirectory(directory);
        checkFile(newVideo, cFormatNewVideo, directory, false);
        checkFile(dirVideoToMerge, true);
        checkFile(dirAudioToMerge, true);

        List<String> command = new ArrayList<>();
        command.addAll(Arrays.asList(this.ffmpegDirectory, "-i", dirVideoToMerge));
        command.addAll(Arrays.asList("-i", dirAudioToMerge));
        command.addAll(Arrays.asList("-c:v", "copy"));
        if(cFormatNewVideo.equals(FfmpegContainerFormat.mkv)) {
            command.addAll(Arrays.asList("-c:a", "mp3", "-b:a", "32k", "-filter:a", "volume=1.5"));
        }
        if(cFormatNewVideo.equals(FfmpegContainerFormat.mp4)) {
            command.addAll(Arrays.asList("-c:a", "aac", "-b:a", "32k", "-strict", "-2", "-filter:a", "volume=1.5"));
        }
        command.addAll(Arrays.asList("-map", "0:v:0", "-map", "1:a:0"));
        command.addAll(Arrays.asList("-shortest", directory + "/" + newVideo + "." + cFormatNewVideo));
        command = IsoToUtf.windowsConvert(command);
        logCommand(command);
        ProcessBuilder pb = new ProcessBuilder(command);
        return pb.start();
    }

    @Override
    public Process executeFfmpegCutVideo(String dirVideoToCut, String directoryCutVideos, VideoCutInfo videoCutInfo, FfmpegContainerFormat cFormat) throws ICommandException, IOException {
        checkFile(dirVideoToCut, true);
        checkDirectory(directoryCutVideos);
        System.out.println();

        List<String> command = new ArrayList<>();
        int index = 0;
        List<Cut> cuts = videoCutInfo.getCuts();
        if(cuts.size() == 0) {
            throw new ICommandNotCutsException("There's no cuts on VideoInfo");
        }
        PrintWriter writer = new PrintWriter(directoryCutVideos + "/files.txt", "UTF-8");
        command.addAll(Arrays.asList(this.ffmpegDirectory, "-i", dirVideoToCut, "-vcodec", "copy"));
        for(Cut cut: cuts) {
            String cuttedVideo = directoryCutVideos + "/out" + index + "." + cFormat;
            command.addAll(Arrays.asList("-acodec", "copy", "-ss", cut.getStart(), "-to", cut.getEnd(), cuttedVideo));
            writer.println("file '" + "out" + index + "." + cFormat + "'");
            index++;
        }
        writer.close();
        command = IsoToUtf.windowsConvert(command);
        logCommand(command);
        ProcessBuilder pb = new ProcessBuilder(command);
        return pb.start();
    }

    @Override
    public Process executeMergeVideos(FfmpegContainerFormat cFormat, String newVideo, String directory, String directoryVideos) throws ICommandException, IOException {
        checkDirectory(directory);
        checkFile(newVideo, cFormat, directory, false);
        checkDirectory(directoryVideos);

        File tempFile = new File(directoryVideos);
        String[] files = tempFile.list();
        if(files.length == 0) {
            throw new ICommandNoVideosCutException("You should cut a video before using executeFfmpegCutVideo");
        }
        List<String> command = new ArrayList<>();
        command.addAll(commandBackSlashToForwardSlash(Arrays.asList(this.ffmpegDirectory, "-f", "concat", "-i", directoryVideos + "/files.txt")));
        command.addAll(IsoToUtf.windowsConvert(Arrays.asList("-c", "copy", directory + "/" + newVideo + "." + cFormat)));
        logCommand(command);
        ProcessBuilder pb = new ProcessBuilder(command);
        return pb.start();
    }

    @Override
    public Process createThumbnail(String dirVideo, String imageName, String finalDirectory) throws ICommandException, IOException {
        File file = new File(dirVideo);
        checkDirectory(finalDirectory);
        if(!file.exists()) {
            throw new ICommandFileNotExistException("The file doesn't exists");
        }
        String thumbnailDir = finalDirectory + "/" + imageName + ".jpg";
        List<String> command = new ArrayList<>();
        command.addAll(Arrays.asList(this.ffmpegDirectory, "-i", file.getPath(), "-vf", "scale=640:360", "-ss", "00:00:01.000", "-vframes", "1", thumbnailDir, "-y"));
        command = IsoToUtf.windowsConvert(command);
        logCommand(command);
        ProcessBuilder pb = new ProcessBuilder(command);
        return pb.start();
    }

    private String extractDefaultAudioDevice() throws IOException {
        List<String> command = new ArrayList<>();
        command.addAll(Arrays.asList(this.ffmpegDirectory, "-list_devices", "true", "-f", "dshow", "-i", "dummy"));
        log.info("Getting default audio device");

        ProcessBuilder pb = new ProcessBuilder(command);
        Process p = pb.start();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(p.getErrorStream()));

        String output = "";
        String defaultAudioDevice = "";
        while((output = bufferedReader.readLine()) != null) {
            if(output.contains("DirectShow audio devices")) {
                defaultAudioDevice = getTextBetweenDoubleQuotes(bufferedReader.readLine());
                log.info("Default audio device: " + defaultAudioDevice);
            }
        }
        return defaultAudioDevice;
    }

    private void logCommand(List<String> command) {
        String commandLog = "";
        for(String c: command) {
            commandLog += c + " ";
        }
        log.info(commandLog);
    }


    private void checkDirectory(String directory) {
        File directoryFile = new File(directory);
        if (!directoryFile.exists()) {
            directoryFile.mkdir();
        }

    }

    private void checkFile(String name, FfmpegFormat format, String directory, boolean checkExist) throws ICommandFileExistException, ICommandFileNotExistException {
        File checkFile = new File(directory + "/" + name + "." + format);
        if(checkExist) {
            if(!checkFile.exists()) {
                throw new ICommandFileNotExistException("The file: " + name + "." + format + ", doesn't exists");
            }
        }
        else {
            if(checkFile.exists()) {
                throw new ICommandFileExistException("The file: " + name + "." + format + ", actually exist");
            }
        }
    }

    private void checkFile(String dirFile, boolean checkExist) throws ICommandFileExistException, ICommandFileNotExistException {
        File checkFile = new File(dirFile);
        if(checkExist) {
            if(!checkFile.exists()) {
                throw new ICommandFileNotExistException("The file: " + dirFile +", doesn't exists");
            }
        }
        else {
            if(checkFile.exists()) {
                throw new ICommandFileExistException("The file: " + dirFile + ", actually exist");
            }
        }
    }

    /**
     * Ffmpeg has an issue with -concat command, so it's necessary in windows to use
     * forwardSlash for the file containing cuts
     */
    private List<String> commandBackSlashToForwardSlash(List<String> command) {
        return command.stream()
                .map(c -> c.replace("\\", "/"))
                .collect(Collectors.toList());
    }

    private String getTextBetweenDoubleQuotes(String text) {
        text = text.substring(text.indexOf("\"") + 1);
        text = text.substring(0, text.indexOf("\""));
        return text;
    }
}
