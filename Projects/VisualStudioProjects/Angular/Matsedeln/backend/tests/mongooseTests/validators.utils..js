exports.validateNotEmpty = function(received) {
    expect(received).not.toBeNull();
    expect(received).not.toBeUndefined();
    expect(received).toBeTruthy();
};

exports.validateStringEquality = function(received, expected) {
    expect(received).not.toEqual('anythingkjjd');
    expect(received) == (expected);
};

exports.validateMongoDuplicationError = function(name, code) {
    expect(name).not.toEqual('/dummy/i');
    expect(name).toEqual('ValidationError');
    //expect(code).not.toBe(225);
    //expect(code).toBe(11000);
};