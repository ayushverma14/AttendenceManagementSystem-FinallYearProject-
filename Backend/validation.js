const Ajv = require("ajv")
const addFormats = require("ajv-formats")
const ajv = new Ajv()
addFormats(ajv)

const registerValidation = (body)=>{
    const schema = {
        "type": "object",
        "properties": {
          "name": {
            "type": "string",
            "minLength": 4,
            "maxLength": 255
          },
          "email": {
            "type": "string",
            "minLength": 6,
            "maxLength": 255,
            "format": "email"
          },
          "password": {
            "type": "string",
            "minLength": 6,
            "maxLength": 1024
          },
          "date": {
              "type": "string",
              "format": "date"
          },
          "department": {
            "type": "string",
            "minLength": 2,
            "maxLength": 255
          },
          "semester": {
            "type": "string",
          },
          "contact": {
            "type": "string",
            "minLength": 8,
            "maxLength": 14
          }
        },
        "required": [
          // "name",
          "email",
          "password",
          // "contact",
          // "department",
          // "semester"
        ]
    }
    const valid = ajv.validate(schema, body)
    var error=ajv.errors;
    if(!valid){
        error = ajv.errors[0].message
    }

    return {
        valid,
        error
    }
}

const loginValidation = (body)=>{
    const schema = {
        "type": "object",
        "properties": {
          "email": {
            "type": "string",
            "minLength": 6,
            "maxLength": 255,
            "format": "email"
          },
          "password": {
            "type": "string",
            "minLength": 6,
            "maxLength": 1024
          }
        },
        "required": [
          "email",
          "password"
        ]
    }
    const valid = ajv.validate(schema, body)
    var error=ajv.errors;
    if(!valid){
        error = ajv.errors[0].message
    }

    return {
        valid,
        error
    }
}

const attendanceValidation = (body)=>{
  const schema = {
      "type": "object",
      "properties": {
        "department": {
          "type": "string",
          "minLength": 2,
          "maxLength": 255
        },
        "semester": {
          "type": "string"
        },
        "date": {
            "type": "string",
        },
        "pictures": {
          "type": "array"
        }
      },
      "required": [
        "department",
        "semester",
        "date",
        "pictures"
      ]
  }
  const valid = ajv.validate(schema, body)
  var error=ajv.errors;
  if(!valid){
      error = ajv.errors[0].message
  }

  return {
      valid,
      error
  }
}

module.exports.registerValidation = registerValidation;
module.exports.loginValidation = loginValidation;
module.exports.attendanceValidation = attendanceValidation;