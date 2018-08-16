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

gulp.task('install-dependencies', () => new Promise((resolve, reject) => {
    let npm_install = spawn('npm', ['install'], {
        cwd: projectRoot.classRecTeacherPcFrontend(),
        shell: true,
        stdio: 'inherit'
    });

    log('Installing dependencies in ' + projectRoot.classRecTeacherPcFrontend());

    npm_install.on('error', (error) => {
        log.error(error);
        reject();
    });

    npm_install.on('close', (code) => {
        if(code === 0) {
            log.info(`Finished depencencies install`); 
            resolve();
        }
        else {
            log.error(`npm install in ${projectRoot.classRecTeacherPcFrontend()} finished with code ${code}`);
            reject();
        }
    });
}));

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
    let command = path.join(projectRoot.classRecTeacherPcFrontend(), 'node_modules/.bin/ng build --prod --aot');
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
    let command = path.join(projectRoot.classRecTeacherPcFrontend(), 'node_modules/.bin/ng serve --proxy-config proxy.conf.json');
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

gulp.task('docker-build-teacher-pc', () =>  new Promise((resolve, reject) => {
    let docker_build_pc = spawn('docker', ['build', '-t', 'cruizba/class-recorder', '.'], {
        cwd: projectRoot.builds.classRecTeacherPcServer(),
        shell: true,
        stdio: 'inherit'
    });

    log('Building docker image of class-recorder-teacher-pc-server');

    docker_build_pc.on('error', (error) => {
        log.error(error);
        reject();
    });

    docker_build_pc.on('close', (code) => {
        if(code === 0) {
            log.info(`Finished docker build`); 
            resolve();
        }
        else {
            log.error(`docker build finished with code ${code}`);
            reject();
        }
    });

}));

gulp.task('docker-push-teacher-pc', () => new Promise((resolve, reject) => {
    let docker_push_pc = spawn('docker', ['push', 'cruizba/class-recorder'], {
        cwd: projectRoot(),
        shell: true,
        stdio: 'inherit'
    });

    log('Pushing to docker-hub cruizba/class-recorder docker image of class-recorder-teacher-pc-server');

    docker_push_pc.on('error', (error) => {
        log.error(error);
        reject();
    });

    docker_push_pc.on('close', (code) => {
        if(code === 0) {
            log.info(`Finished docker push`); 
            resolve();
        }
        else {
            log.error(`docker push finished with code ${code}`);
            reject();
        }
    });
}));

gulp.task('docker-login', () => new Promise((resolve, reject) => {
    let docker_push_pc = exec('echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin', {
        cwd: projectRoot(),
        shell: true,
        stdio: 'inherit'
    });

    log('Log in to docker hub');

    docker_push_pc.on('error', (error) => {
        log.error(error);
        reject();
    });

    docker_push_pc.on('close', (code) => {
        if(code === 0) {
            log.info(`Finished docker login`); 
            resolve();
        }
        else {
            log.error(`docker login finished with code ${code}`);
            reject();
        }
    });
}));

gulp.task('build', gulp.series('build-pc-frontend', 'build-pc-server'));

gulp.task('build-docker-pc-server', gulp.series('build', 'docker-build-teacher-pc'));

gulp.task('travis-script', gulp.series('install-dependencies', 'build', 'docker-build-teacher-pc', 'docker-login', 'docker-push-teacher-pc'));