<div align="center">
    <h1>HASE</h1>
    <img src="docs/logo.svg" />
    <h2>A calendar for you and your study group.</h2>
</div>

### About 

HASE is a web application which helps you organize your study group by having a common pool of appointments. Within that, everyone can choose
their dates and include them into their individual calendar.

It is a project for the module "Programming 3" at the University of Applied Sciences Saarland and is being developed by **H**anan Ahmad Ashir,
**A**msakan Bavan, **S**ophia Sarkhovska and **E**rwin Holzhauser.

### Build and Run

We have to build two components for HASE. The frontend, which will be displayed on your web browser
and the backend that runs in the background for managing your appointments and users.

#### Step 1: Clone the HASE repository

This can be done by copying the URL of this repository from the browser navigation bar and running
git clone on it, as shown below.

![](./docs/clone.gif)

#### Step 2: Build the backend

The backend can be build by doing the following:

```bash
cd hase
cd backend
mvn clean install
```

![](./docs/backend.gif)

This will run the building process for the backend. The binary of the backend will then be stored in the 
target directory. After successful building of the backend, you can run

```bash
cd target
java -jar hasev2-*.jar
```

![](./docs/runBackend.gif)

And the backend will be started.

#### Step 3: Run the frontend

Now that we have built the backend, we can run the frontend and use HASE. This can be done by
doing the following:

````bash
cd ../frontend
npm install
npm run preview
````

![](./docs/runFrontend.gif)

This will start up a local web server which will be available under the given address and
then can be accessed from your web browser.

**Congratulations!**<br>
You have build HASE and now have a running instance of it! 😀
