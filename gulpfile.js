const path = require('path');
const fs = require('fs-extra');
const gulp = require('gulp');
const log = require('fancy-log');
const { spawn, exec } = require('child_process');

const projectRoot = () => process.cwd();

/* ============================================= DIRECTORIES ============================================= */

/*
 * Directories pc server
 */
projectRoot.classRecTeacherPcServer = () => path.join(projectRoot(), 'class-recorder-teacher-pc-server');
projectRoot.classRecTeacherPcServer.src = () => path.join(projectRoot.classRecTeacherPcServer(), 'src/main');
projectRoot.classRecTeacherPcServer.src.staticResources = () => path.join(projectRoot.classRecTeacherPcServer.src(), 'resources/static');
projectRoot.classRecTeacherPcServer.build = () => path.join(projectRoot.classRecTeacherPcServer(), 'target');

/*
 * Directories pc frontend
 */
projectRoot.classRecTeacherPcFrontend = () => path.join(projectRoot(), 'class-recorder-teacher-pc-frontend');
projectRoot.classRecTeacherPcFrontend.build = () => path.join(projectRoot.classRecTeacherPcFrontend(), 'dist/class-recorder-teacher-pc-frontend');

/* Directories pc mobile app */
projectRoot.classRecTeacherPcMobile = () => path.join(projectRoot(), 'class-recorder-teacher-pc-mobile');
projectRoot.classRecTeacherPcMobile.debug_build = () =>
    path.join(projectRoot.classRecTeacherPcMobile(), 'platforms/android/app/build/outputs/apk/debug');
/*
* Directories builds
*/
projectRoot.builds = () => path.join(projectRoot(), 'build-binaries');

/* ============================================= GULP TASKS ============================================= */

gulp.task('install-dependencies-pc-frontend', () => new Promise((resolve, reject) => {
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

gulp.task('install-dependencies-mobile-npm', () => new Promise((resolve, reject) => {
    let npm_install = spawn('npm', ['install'], {
        cwd: projectRoot.classRecTeacherPcMobile(),
        shell: true,
        stdio: 'inherit'
    });

    log('Installing dependencies in ' + projectRoot.classRecTeacherPcMobile());

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
            log.error(`npm install in ${projectRoot.classRecTeacherPcMobile()} finished with code ${code}`);
            reject();
        }
    });
}));

gulp.task('install-dependencies-mobile-cordova', () => new Promise((resolve, reject) => {
    let cordova = spawn('ionic', ['cordova', 'prepare'], {
        cwd: projectRoot.classRecTeacherPcMobile(),
        shell: true,
        stdio: 'inherit'
    });

    log('Installing cordova plugins in ' + projectRoot.classRecTeacherPcMobile());

    cordova.on('error', (error) => {
        log.error(error);
        reject();
    });

    cordova.on('close', (code) => {
        if(code === 0) {
            log.info(`Finished install cordova plugins`);
            resolve();
        }
        else {
            log.error(`ionic state restore, in ${projectRoot.classRecTeacherPcMobile()} finished with code ${code}`);
            reject();
        }
    });
}));

gulp.task('build-mobile-app', () => new Promise((resolve, reject) => {
    let cordova = spawn('ionic', ['cordova', 'build', 'android', '--prod', '--verbose'], {
        cwd: projectRoot.classRecTeacherPcMobile(),
        shell: true,
        stdio: 'inherit'
    });

    log('Installing cordova plugins in ' + projectRoot.classRecTeacherPcMobile());

    cordova.on('error', (error) => {
        log.error(error);
        reject();
    });

    cordova.on('close', (code) => {
        if(code === 0) {
            let dir = projectRoot.classRecTeacherPcMobile.debug_build();
            fs.readdirSync(dir).filter((file) => file.endsWith('.apk')).forEach((file) => {
                let build = path.join(dir, file);
                let destination = path.join(projectRoot.builds(), 'class-recorder.apk');

                fs.copySync(build, destination);

                log.info(`Finished packaging ${file}`);
            });
            log.info(`Finished building apk`);
            resolve();
        }
        else {
            log.error(`ionic cordova build android --prod --release, in
            ${projectRoot.classRecTeacherPcMobile()} finished with code ${code}`);
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
                let destination = path.join(projectRoot.builds(), 'class-recorder-pc.jar');

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

gulp.task('test-pc-server', () => new Promise((resolve, reject) => {
    let test_server = spawn('mvn', ['test'], {
        cwd: projectRoot.classRecTeacherPcServer(),
        shell: true,
        stdio: 'inherit'
    });

    log('Started test of class-recorder-teacher-pc-server');

    test_server.on('error', (error) => {
        log.error(error);
    })

    test_server.on('close', (code) => {
        if(code === 0) {
            resolve();
        }
        else {
            log.error(`maven finished with error code ${code}`);
            reject();
        }
    })
}));

gulp.task('dev:start-pc-server', () => new Promise((resolve, reject) => {
    let start_server = spawn('mvn', ['spring-boot:run', '-Dspring-boot.run.profiles=dev'], {
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

gulp.task('dev:start-pc-server-electron', () => new Promise((resolve, reject) => {
    let start_server = spawn('mvn', ['spring-boot:run', '-Dspring-boot.run.profiles=electron'], {
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

gulp.task('test-pc-frontend', () => new Promise((resolve, reject) => {
    let command = path.join(projectRoot.classRecTeacherPcFrontend(), 'node_modules/.bin/ng e2e');
    let test_frontend = exec(command, {
        cwd: projectRoot.classRecTeacherPcFrontend(),
        shell: true,
        stdio: 'inherit'
    });

    log('Started test of class-recorder-frontend');

    test_frontend.on('error', (error) => {
        log.error(error);
    });

    test_frontend.stderr.on('data', (data) => {
        log.error(data);
    })

    test_frontend.stdout.on('data', (data) => {
        console.log(data);
    })

    test_frontend.on('close', (code) => {
        if(code === 0) {
            log.info(`Angular test finished`);
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

gulp.task('dev:start-mobile-device', () => new Promise((resolve, reject) => {
    let cordova = spawn('ionic', ['cordova', 'run', 'android'], {
        cwd: projectRoot.classRecTeacherPcMobile(),
        shell: true,
        stdio: 'inherit'
    });

    cordova.on('error', (error) => {
        log.error(error);
    })

    cordova.on('close', (code) => {
        if(code === 0) {
            log('Done!');
            resolve()
        }
        else {
            log.error(`ionic debug finished with error code ${code}`);
            reject();
        }
    })
}));

gulp.task('dev:start-mobile-serve', () => new Promise((resolve, reject) => {
    let cordova = spawn('npm', ['run', 'start'], {
        cwd: projectRoot.classRecTeacherPcMobile(),
        shell: true,
        stdio: 'inherit'
    });

    cordova.on('error', (error) => {
        log.error(error);
    })

    cordova.on('close', (code) => {
        if(code === 0) {
            log('Done!');
            resolve()
        }
        else {
            log.error(`ionic serve finished with error code ${code}`);
            reject();
        }
    })
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

gulp.task('install-dependencies', gulp.series(
    'install-dependencies-pc-frontend',
    'install-dependencies-mobile-npm'));

gulp.task('build', gulp.series(
    'build-pc-frontend',
    'build-pc-server',
    'build-mobile-app'));

gulp.task('build-no-mobile', gulp.series(
    'build-pc-frontend',
    'build-pc-server'
));

gulp.task('build-docker-pc-server', gulp.series(
    'build',
    'docker-build-teacher-pc'));

gulp.task('travis-script', gulp.series(
    'install-dependencies',
    'build',
    'test-pc-server',
    'test-pc-frontend',
    'docker-build-teacher-pc',
    'docker-login',
    'docker-push-teacher-pc'));
