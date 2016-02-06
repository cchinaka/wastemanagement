# grunt-maven-plugin

**grunt-maven-plugin** plugin allows to integrate **Grunt** tasks into **Maven** build process. **Grunt** [http://gruntjs.com/] is the JavaScript task runner utility. **grunt-maven-plugin** works on both Windows and *nix systems.

## Prerequisites

The only required dependency is **nodejs** [http://nodejs.org/] with **npm**.
Globally installed **grunt-cli** [http://gruntjs.com/getting-started] is optional and preferred, but not necessary, as installing custom node modules can be problematic in some environments (ex. CI servers). Additional configuration is needed when using local **grunt-cli**.

## Usage

Add **grunt-maven-plugin** to application build process in your *pom.xml*:

```xml
<plugin>
    <groupId>pl.allegro</groupId>
    <artifactId>grunt-maven-plugin</artifactId>
    <version>1.0.0</version>
    <configuration>
	<!-- relative to src/main/webapp/, default: static -->
        <jsSourceDirectory>path_to_js_project</jsSourceDirectory>
    </configuration>
    <executions>
        <execution>
            <goals>
                <goal>create-resources</goal>
                <goal>npm</goal>
                <goal>grunt</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

### Usage with local grunt-cli

In order to use local **grunt-cli**, add

```xml
<gruntExecutable>node_modules/grunt-cli/bin/grunt</gruntExecutable>
<runGruntWithNode>true</runGruntWithNode>
```

options to plugin configuration and add **grunt-cli** to JS project **package.json**:

```javascript
{
    "devDepenencies": {
        "grunt-cli": "~0.1.7",
        "grunt": "~0.4.1"
        /*...*/
    }
}
```

## How it works?

1. JavaScript project sources from

        sourceDirectory/jsSourceDirectory (default: src/main/webapp/static)

    are copied to

        gruntBuildDirectory (default: target-grunt)

    Copied directory has to contain **package.json** and **Gruntfile.js** files

1. `npm install` is called, fetching all dependencies

1. `grunt` is run to complete build process

Since plugin creates own target dir, it should be added to ignored resources in SCM configuration (like .gitignore).

## grunt-maven-plugin options

Plugin options available in `<configuration>...</configuration>` are:

* **target** : name of Grunt target to run (defaults to null, default Grunt target is run)
* **gruntBuildDirectory** : path to Grunt build directory (target for Grunt); defaults to *${basedir}/target-grunt*
* **sourceDirectory** : path to directory containing source JavaScript project directory; defaults to *${basedir}/src/main/webapp*
* **jsSourceDirectory** : name of directory relative to *sourceDirectory*, which contents are going to be copied to *jsTargetDirectory*; defaults to *static*
* **nodeExecutable** : name of globally available **node** executable; defaults to *node*
* **gruntExecutable** : name of **grunt** executable; defaults to *grunt*
* **runGruntWithNode** : if Grunt executable is a js script, it needs to be run using node, ex: `node path/to/grunt`; defaults to *false*
* **npmExecutable** : name of globally available **npm** executable; defaults to *npm*

## Execution goals

* **create-resources** : copies resources from *sourceDirectory/jsSourceDirectory* to *gruntBuildDirectory*
* **npm** : executes `npm install` in target directory
* **grunt** : executes Grunt in target directory
* **clean** : deletes *gruntBuildDirectory*

## Integrated workflow

Using grunt-maven-plugin is convenient, but there is still a huge gap between frontend and backend development workflow. Various IDEs (Netbeans, IntelliJ Idea, Eclipse)
try to ease webapp development by synchronizing changes made in *src/webapp/* to exploded WAR directory in *target/*. This naive approach works as long as there is no
need to minify JavaScript sources, compile less files or project does not follow "single WAR" rule and can afford using Maven profiles. Rebuilding project each time a
change was made in *.js is a horrible thing. Fortunately grunt-maven-plugin is a great tool to solve this problem.

Idea is to ignore IDE mechanisms and run Grunt build each time a change in static files was made. Traditional workflow looks like:

1. user changes *src/webapp/static/hello.js*
1. IDE detects change
1. IDE copies *hello.js* to *target/_war_name_/static/hello.js*

This gives little room to integrate other processes in between. Workflow utilizing **grunt-maven-plugin**:

1. run [Grunt watch process](https://github.com/gruntjs/grunt-contrib-watch)
1. user changes *src/webapp/static/hello.js*
1. Grunt detects changes, copies *hello.js* to *target-grunt/hello.js*
1. run Grunt tasks, produce *target-grunt/dist/hello.min.js* with source map *target-grunt/dist/hello.map.js*
1. Grunt copies results to *target/_war_name_/static*

Now what happens inside *target-grunt* is for us to decide, it can be virtually anything - minification, less compilation, running
JS tests, JS linting. Anything Grunt can do, just like during *heavy* build process.

### Configuring Maven

Since we want grunt-maven-plugin to take control of what ends up in WAR, we need to tell Maven WAR plugin to ignore our statics dir when creating WAR:

```xml
<build>
    <plugins>
	<plugin>
	    <artifactId>maven-war-plugin</artifactId>
	    <version>2.3</version>
	    <configuration>
		<warSourceExcludes>jsSourceDirectory/**</warSourceExcludes>
	    </configuration>
	</plugin>
    </plugins>
</build>
```

### Configuring Grunt

There are two Grunt modules that need to be imported: **grunt-contrib-watch** and **grunt-contrib-copy**. Configuration:

```javascript
/*...*/
    copy: {
	targetGrunt: { // copy from webapp static src to target-grunt
	    files: [ { expand: true, cwd: '../src/main/webapp/static/', src: './**', dest: './' } ]
	},
	dist: { // copy results of Grunt compilation to target-grunt/dist
	    files: [ { expand: true, src: ['app.min.js', 'js/**', '!js/test/**'], dest: 'dist/' } ]
	},
	targetMaven: { // copy dist files to exploded WAR target
	    files: [ { expand: true, cwd: './dist', src: ['./**'], dest: '../target/<war_name>/static/' } ]
	}
    },
    /*...*/
    watch: {
	maven: { // observe files in webapp static src
	    files: '../src/main/webapp/static/**',
            tasks: ['copy:targetGrunt', 'default']
        }
    },
/*...*/

// this is a standard build task
grunt.registerTask('default', ['jshint', 'karma', 'less', 'uglify' 'copy:dist', 'copy:targetMaven']);
```

Of course you can adjust what tasks are run on file change, maybe there is no need to recompile less or to run tests.

### Configuring IDE

Depending on IDE, synchronization processes behave differently. Having JSP files synchronized is a nice feature, so it is up to you
to decide what should be turned off. In Netbeans and IntelliJ no configuration is needed, they play nicely with new workflow.

You still have to remember to run Grunt watch process in background so it can monitor changes. It can be run from IDE via grunt-maven-plugin.
Define custom runner that will run:

    mvn grunt:grunt -Dtarget=watch

You should see process output each time static sources change.

## License

**grunt-maven-plugin** is published under [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).
