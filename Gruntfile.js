module.exports = function (grunt) {
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),

        concat: {
            options: {
                separator: ";\n"
            },
            websiteApp: {
                src: [
                    'src/main/webapp/battleComponent/*.js',
                    'src/main/webapp/battleComponent/**/*.js'
                ]
                , dest: 'src/main/webapp/WarOfHeroesApp.js'
            }
        },
        watch: {
            scripts: {
                files: ['src/main/webapp/battleComponent/**'],
                tasks: ['concat'],
                options: {
                    spawn: false,
                },
            }
        }
    });

    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-watch');

    grunt.registerTask('default', ['concat', 'watch']);

};