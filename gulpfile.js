const path = require('path');
const fs = require('fs-extra');
const gulp = require('gulp');
const log = require('fancy-log');
const { spawn, exec } = require('child_process');

const projectRoot = () => process.cwd();

projectRoot.classRecTeacherPcServer = () => path.join(projectRoot(), 'class-recorder-teacher-pc-server');
projectRoot.classRecTeacherPcServer.src = () => path.join(projectRoot.classRecTeacherPcServer(), 'src/main');
projectRoot.classRecTeacherPcServer.src.staticResources = () => path.join(projectRoot.classRecTeacherPcServer.src(), 'resources/static');
projectRoot.classRecTeacherPcServer.build = () => path.join(projectRoot.classRecTeacherPcServer(), 'target');


projectRoot.classRecTeacherPcFrontend = () => path.join(projectRoot(), 'class-recorder-teacher-pc-frontend');
projectRoot.classRecTeacherPcFrontend.build = () => path.join(projectRoot.classRecTeacherPcFrontend(), 'dist/class-recorder-teacher-pc-frontend'); 

projectRoot.builds = () => path.join(projectRoot(), 'builds');
projectRoot.builds.classRecTeacherPcServer = () => path.join(projectRoot.builds(), 'class-recorder-teacher-pc-server');

gulp.task('build-pc-server', () => new Promise((resolve, reject) => {
    let build_server = spawn('mvn', ['clean', 'package'], {
        cwd: projectRoot.classRecTeacherPcServer(),
        shell: true,
        stdio: 'inherit'
    });

    log('Started build of class-recorder-teacher-pc-server');

    build_server.on('error', (error) => {
        log.error(error);
    })

    build_server.on('close', (code) => {
        if(code === 0) {
            let dir = projectRoot.classRecTeacherPcServer.build();
            fs.readdirSync(dir).filter((file) => file.endsWith('.jar')).forEach((file) => {
                let build = path.join(dir, file);
                let destination = path.join(projectRoot.builds.classRecTeacherPcServer(), 'classrecorderpc.jar');

                fs.copySync(build, destination);

                log.info(`Finished packaging ${file}`);
            });
            resolve();
        }
        else {
            log.error(`maven finished with error code ${code}`);
            reject();
        }
    })
}));

gulp.task('dev:start-pc-server', () => new Promise((resolve, reject) => {
    let start_server = spawn('mvn', ['spring-boot:run'], {
        cwd: projectRoot.classRecTeacherPcServer(),
        shell: true,
        stdio: 'inherit'
    });

    start_server.on('error', (error) => {
        log.error(error);
    })

    start_server.on('close', (code) => {
        if(code === 0) {
            log('Done!');
            resolve()
        }
        else {
            log.error(`maven finished with error code ${code}`);
            reject();
        }
    })
}));

gulp.task('build-pc-frontend', () => new Promise((resolve, reject) => {
    let command = 'ng build --prod --aot';
    let build_frontend = exec(command, {
        cwd: projectRoot.classRecTeacherPcFrontend(),
        shell: true,
        stdio: 'inherit'
    });

    log('Started build of class-recorder-frontend');

    build_frontend.on('error', (error) => {
        log.error(error);
    });

    build_frontend.stderr.on('data', (data) => {
        log.error(data);
    })

    build_frontend.on('close', (code) => {
        if(code === 0) {
            let originDir = projectRoot.classRecTeacherPcFrontend.build();
            let destinDir = projectRoot.classRecTeacherPcServer.src.staticResources();
            fs.readdirSync(originDir).forEach((file) => {
                let origin = path.join(originDir, file);
                let destination = path.join(destinDir, file);

                fs.copySync(origin, destination);

                log.info(`Copied ${origin} to ${destination}`);
            })
            log.info(`Angular build finished`);
            resolve();
        }
        else {
            log.error(`ng finished with error code ${code}`);
            reject();
        }
    });
}));

gulp.task('dev:start-pc-frontend', () =>  new Promise((resolve, reject) => {
    let command = 'ng serve --proxy-config proxy.conf.json';
    let start_frontend = exec(command, {
        cwd: projectRoot.classRecTeacherPcFrontend(),
        shell: true,
        stdio: 'inherit'
    });

    log('Started class-recorder-frontend');

    start_frontend.on('error', (error) => {
        log.error(error);
    });

    start_frontend.stderr.on('data', (data) => {
        log.error(data);
    })

    start_frontend.on('close', (code) => {
        if(code === 0) {
            log('Done!');
            resolve();
        }
        else {
            log.error(`ng finished with error code ${code}`);
            reject();
        }
    });

    start_frontend.stdout.on('data', (data) => {
        log(data);
    });
}));

gulp.task('build', gulp.series('build-pc-frontend', 'build-pc-server'));
