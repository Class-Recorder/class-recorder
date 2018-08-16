import {FfmpegContainerFormat} from '../classes/ffmpeg/FfmpegContainerFormat';

export class VideoToRecInfo {
    ffmpegContainerFormat: string;
    frameRate: number;
    videoName: string;
    webcam: boolean;
}
