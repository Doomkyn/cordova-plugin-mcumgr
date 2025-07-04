var exec = require('cordova/exec');
/*
exports.performDfu = function (params, success, error) {
    exec(success, error, 'McumgrPlugin', 'performDfu', [params]);
};
*/
exports.testLog = function(success, error) {
    exec(success, error, 'McumgrPlugin', 'testLog', []);
};