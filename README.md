[![CircleCI](https://circleci.com/gh/lparisot/rfb-mono.svg?style=svg)](https://circleci.com/gh/lparisot/rfb-mono)
[![codecov](https://codecov.io/gh/lparisot/rfb-mono/branch/master/graph/badge.svg)](https://codecov.io/gh/lparisot/rfb-mono)

# RFB
This application was generated using JHipster 4.10.2, you can find documentation and help at [http://www.jhipster.tech/documentation-archive/v4.10.2](http://www.jhipster.tech/documentation-archive/v4.10.2).

## Functional requirements

* Users can login by email / password or FaceBook
* Facebook login should capture email
* Need lost password email
* Need to track RFB run locations, and schedule
* System should create a unique code for runners to sign into run
* System need to support multiple security roles:
    * root - system admin
    * admin - super user for application
        * admin user can promote  demote users to admin or organizer users
        * admin user can create run locations
        * admin user can set scheduled runs
        * admin user can access unique run code
    * organizer - able to access unique run code
        * organizer user can access unique run code
    * runner - able to enter unique run code
    * all user can:
        * change their password
        * change their email
        * set home location
* Each location will show a leader board of users
* After 10 failed login attempts, system will lock out user account for 10 minutes
* System will require a minimum password length of 6 characters
* System will automatically generate a unique code for each run event
* When use enters run code, system will log code used, user and date time

## Environment variables

If you want to activate social authentication, you must provide environment variables:
* SPRING_SOCIAL_FACEBOOK_CLIENT_ID and SPRING_SOCIAL_FACEBOOK_CLIENT_SECRET
* SPRING_SOCIAL_GOOGLE_CLIENT_ID and SPRING_SOCIAL_GOOGLE_CLIENT_SECRET
* SPRING_SOCIAL_TWITTER_CLIENT_ID and SPRING_SOCIAL_TWITTER_CLIENT_SECRET

## Development

Before you can build this project, you must install and configure the following dependencies on your machine:

1. [Node.js][]: We use Node to run a development web server and build the project.
   Depending on your system, you can install Node either from source or as a pre-packaged bundle.
2. [Yarn][]: We use Yarn to manage Node dependencies.
   Depending on your system, you can install Yarn either from source or as a pre-packaged bundle.

After installing Node, you should be able to run the following command to install development tools.
You will only need to run this command when dependencies change in [package.json](package.json).

    yarn install

We use yarn scripts and [Webpack][] as our build system.


Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

    ./mvnw
    yarn start

[Yarn][] is also used to manage CSS and JavaScript dependencies used in this application. You can upgrade dependencies by
specifying a newer version in [package.json](package.json). You can also run `yarn update` and `yarn install` to manage dependencies.
Add the `help` flag on any command to see how you can use it. For example, `yarn help update`.

The `yarn run` command will list all of the scripts available to run for this project.

### Service workers

Service workers are commented by default, to enable them please uncomment the following code.

* The service worker registering script in index.html
```
<script>
    if ('serviceWorker' in navigator) {
        navigator.serviceWorker
        .register('./sw.js')
        .then(function() { console.log('Service Worker Registered'); });
    }
</script>
```
* The copy file option in webpack-common.js
```js
{ from: './src/main/webapp/sw.js', to: 'sw.js' },
```
Note: Add the respective scripts/assets in `sw.js` that is needed to be cached.

### Managing dependencies

For example, to add [Leaflet][] library as a runtime dependency of your application, you would run following command:

    yarn add --exact leaflet

To benefit from TypeScript type definitions from [DefinitelyTyped][] repository in development, you would run following command:

    yarn add --dev --exact @types/leaflet

Then you would import the JS and CSS files specified in library's installation instructions so that [Webpack][] knows about them:

Edit [src/main/webapp/app/vendor.ts](src/main/webapp/app/vendor.ts) file:
~~~
import 'leaflet/dist/leaflet.js';
~~~

Edit [src/main/webapp/content/css/vendor.css](src/main/webapp/content/css/vendor.css) file:
~~~
@import '~leaflet/dist/leaflet.css';
~~~

Note: there are still few other things remaining to do for Leaflet that we won't detail here.

For further instructions on how to develop with JHipster, have a look at [Using JHipster in development][].

### Using angular-cli

You can also use [Angular CLI][] to generate some custom client code.

For example, the following command:

    ng generate component my-component

will generate few files:

    create src/main/webapp/app/my-component/my-component.component.html
    create src/main/webapp/app/my-component/my-component.component.ts
    update src/main/webapp/app/app.module.ts


## Building for production

To optimize the rfb application for production, run:

    ./mvnw -Pprod clean package

This will concatenate and minify the client CSS and JavaScript files. It will also modify `index.html` so it references these new files.
To ensure everything worked, run:

    java -jar target/*.war

Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.

Refer to [Using JHipster in production][] for more details.

## Deliver a release

To add a new tag, run:

    gradle release


## Testing

To launch your application's tests, run:

    ./mvnw clean test

### Client tests

Unit tests are run by [Karma][] and written with [Jasmine][]. They're located in [src/test/javascript/](src/test/javascript/) and can be run with:

    yarn test



For more information, refer to the [Running tests page][].

## Using Docker to simplify development (optional)

You can use Docker to improve your JHipster development experience. A number of docker-compose configuration are available in the [src/main/docker](src/main/docker) folder to launch required third party services.
For example, to start a mysql database in a docker container, run:

    docker-compose -f src/main/docker/mysql.yml up -d

To stop it and remove the container, run:

    docker-compose -f src/main/docker/mysql.yml down

You can also fully dockerize your application and all the services that it depends on.
To achieve this, first build a docker image of your app by running:

    ./mvnw package -Pprod dockerfile:build

Then run:

    docker-compose -f src/main/docker/app.yml up -d

For more information refer to [Using Docker and Docker-Compose][], this page also contains information on the docker-compose sub-generator (`jhipster docker-compose`), which is able to generate docker configurations for one or several JHipster applications.

## Continuous Integration

CircleCI is used to do the continuous integration.

If you want to push the docker image of your project, you must declare three environment variables in your Circle CI project:
* DOCKER_HUB_EMAIL: your docker hub email
* DOCKER_HUB_USER: your docker hub account
* DOCKER_HUB_PWD: your docker hub password

[JHipster Homepage and latest documentation]: http://www.jhipster.tech
[JHipster 4.10.2 archive]: http://www.jhipster.tech/documentation-archive/v4.10.2

[Using JHipster in development]: http://www.jhipster.tech/documentation-archive/v4.10.2/development/
[Using Docker and Docker-Compose]: http://www.jhipster.tech/documentation-archive/v4.10.2/docker-compose
[Using JHipster in production]: http://www.jhipster.tech/documentation-archive/v4.10.2/production/
[Running tests page]: http://www.jhipster.tech/documentation-archive/v4.10.2/running-tests/
[Setting up Continuous Integration]: http://www.jhipster.tech/documentation-archive/v4.10.2/setting-up-ci/


[Node.js]: https://nodejs.org/
[Yarn]: https://yarnpkg.org/
[Webpack]: https://webpack.github.io/
[Angular CLI]: https://cli.angular.io/
[BrowserSync]: http://www.browsersync.io/
[Karma]: http://karma-runner.github.io/
[Jasmine]: http://jasmine.github.io/2.0/introduction.html
[Protractor]: https://angular.github.io/protractor/
[Leaflet]: http://leafletjs.com/
[DefinitelyTyped]: http://definitelytyped.org/
[Gradle Release Plugin]: https://github.com/researchgate/gradle-release
