var exec = require('cordova/exec');

exports.performDfu = function (params, success, error) {
    exec(success, error, 'McumgrPlugin', 'performDfu', [params]);
};
