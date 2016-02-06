module.exports = function(grunt) {
	var resources = grunt.file.readJSON("resources.json");
	//console.log(resources);
	grunt.initConfig({
		pkg : grunt.file.readJSON("package.json"),
		concat : {
			options : {
				separator : ';'
			},
			basic_and_extras: {
				files: {
					'src/main/webapp/static/dist/js/<%= pkg.name %>.landing.js': resources.concat.src.landing,
					'src/main/webapp/static/dist/js/<%= pkg.name %>.js': resources.concat.src.admin,
					'src/main/webapp/static/dist/css/<%=pkg.name%>.landing.css' : resources.cssmin.src.landing,
					'src/main/webapp/static/dist/css/<%=pkg.name%>.css' : resources.cssmin.src.admin
				}
			}
		},
		uglify : {
			options : {
				banner : '/*! <%= pkg.name %> <%= grunt.template.today("yyyy-mm-dd") %> */\n',
				sourceMap : true
			},
			dist : {
				files : {
					'src/main/webapp/static/dist/js/<%= pkg.name %>.landing.min.js' : [ 'src/main/webapp/static/dist/js/<%= pkg.name %>.landing.js' ],
					'src/main/webapp/static/dist/js/<%= pkg.name %>.min.js' : [ 'src/main/webapp/static/dist/js/<%= pkg.name %>.js' ]
				}
			}
		},
		jshint : {
			files: resources.jshint.files,
			options : {
				globals : {
					jQuery : true,
					console : true,
					module : true
				}
			}
		},
		watch : {
			files : [ "<%=jshint.files%>", resources.watch.js, resources.watch.css, resources.watch.misc, 'gruntfile.js', resources.cssmin.src.signin ],
			tasks : [ "jshint", "concat", "uglify", "cssmin" ]
		},
		imagemin : {
			dist : {
				options : {
					optimizationLevel : 5
				},
				files : [ {
					expand : true,
					cwd : 'src/main/webapp/static/stage-images/',
					src : [ '*.{png,jpg,gif}' ],
					dest : 'src/main/webapp/static/dist/images'
				} ]
			}
		},
		cssmin : {
			options: {
				/*processImport: true,*/
				keepSpecialComments: 0
			},
			dist : {
				options : {
					banner : '/*! <%= pkg.name %> <%= grunt.template.today("yyyy-mm-dd") %> */\n',
					report : "gzip"
				},
				files : {
					'src/main/webapp/static/dist/css/<%=pkg.name%>.landing.min.css' : resources.cssmin.src.landing,
					'src/main/webapp/static/dist/css/<%=pkg.name%>.min.css' : resources.cssmin.src.admin,
					'src/main/webapp/static/dist/css/signin.min.css' : resources.cssmin.src.signin
				}
			}
		},
		copy: {
			main: {
				files: [
					{
						expand: true,
						src: ["src/main/webapp/static/template/fonts/**", "src/main/webapp/static/template/admin/fonts/**", "bower_components/components-font-awesome/fonts/**"],
						dest: "src/main/webapp/static/dist/fonts/",
						flatten: true,
						filter: "isFile"
					},
					{
						expand: true,
						src: ["src/main/webapp/static/template/img/**", "src/main/webapp/static/template/admin/images/**", "src/main/webapp/static/template/admin/styles/flat/*.png"],
						dest: "src/main/webapp/static/stage-images/",
						flatten: true,
						filter: "isFile"
					}
				]
			}
		}
	});
	grunt.loadNpmTasks('grunt-contrib-uglify');
	grunt.loadNpmTasks('grunt-contrib-jshint');
	grunt.loadNpmTasks('grunt-contrib-watch');
	grunt.loadNpmTasks('grunt-contrib-concat');
	grunt.loadNpmTasks('grunt-contrib-imagemin');
	grunt.loadNpmTasks('grunt-contrib-cssmin');
	grunt.loadNpmTasks('grunt-contrib-copy');

	grunt.registerTask("default", [ "jshint", "concat", "uglify", "cssmin"]);
	grunt.registerTask("default-and-images", [ "jshint", "concat", "uglify", "cssmin", "copy", "imagemin" ]);
};
