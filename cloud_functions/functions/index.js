const functions = require('firebase-functions');

const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

const DialogflowApp = require('actions-on-google').DialogflowApp;

exports.receiveAssistantRequests = functions.https.onRequest((request, response) => {

    const app = new DialogflowApp({request: request, response: response});

    function handlerRequest(app) {

        const device = app.getArgument('devices');
        const status = app.getArgument('status');

        admin.database().ref(`/JoshsRoom/${device}/value`).set(status == 'true')
            .then(snapshot => {
                app.ask(`Ok, switching ${device} ${status}. Do you want to control anything else?`);
                response.send(200);
            });

    }
    app.handleRequest(handlerRequest);
});