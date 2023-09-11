// genereate service classes from openapi specs

const child_process = require('child_process');
var path = require('path');
var fs = require('fs');


function removeDirectory(dir_path) {
  if (fs.existsSync(dir_path)) {
    fs.readdirSync(dir_path).forEach(function(entry) {
      var entry_path = path.join(dir_path, entry);
      if (fs.lstatSync(entry_path).isDirectory()) {
        removeDirectory(entry_path);
      } else {
        fs.unlinkSync(entry_path);
      }
    });
    fs.rmdirSync(dir_path);
  }
}

function removeFileMatch(dir_path, fileRegex) {
  if (fs.existsSync(dir_path)) {
    fs.readdirSync(dir_path).forEach(function(entry) {
      var entry_path = path.join(dir_path, entry);
      if (fs.lstatSync(entry_path).isDirectory()) {
        removeFileMatch(entry_path, fileRegex);
      } else {
        if (entry.match(fileRegex)) {
          fs.unlinkSync(entry_path);
        }
      }
    });
  }
}

function generateOpenApi(spec, outputDir, additionalProperties) {
  if ((additionalProperties !== null) && (additionalProperties !== undefined)) {
    child_process.execSync(`node ./node_modules/@openapitools/openapi-generator-cli/bin/openapi-generator generate -g typescript-angular -i ${spec} -o ${outputDir} --additional-properties=${additionalProperties}`, {stdio: 'inherit'});
  } else {
    child_process.execSync(`node ./node_modules/@openapitools/openapi-generator-cli/bin/openapi-generator generate -g typescript-angular -i ${spec} -o ${outputDir}`, {stdio: 'inherit'});
  }
}

// Ta bart all genererad kod så att det inte ligger kvar något som inte ska finnas
removeDirectory('./generated');
removeFileMatch('./projects',/.+\.generated.ts/);

// Generera kod från openapi

if (fs.existsSync('../../openapi/admin-api.yaml')) {
  removeDirectory('../generated/admin-api');
  generateOpenApi('../../openapi/admin-api.yaml','./generated/admin-api');
}
if (fs.existsSync('../../openapi/markkoll/markkoll-api.yaml')) {
  removeDirectory('./generated/markkoll-api');
  generateOpenApi('../../openapi/markkoll/markkoll-api.yaml','./generated/markkoll-api', "serviceSuffix=ApiService");
}
if (fs.existsSync('../../openapi/kundconfig-api.yaml')) {
  removeDirectory('./generated/kundconfig-api');
  generateOpenApi('../../openapi/kundconfig-api.yaml','./generated/kundconfig-api');
}
if (fs.existsSync('../../openapi/castor-api.yaml')) {
  generateOpenApi('../../openapi/castor-api.yaml','./generated/castor-api');
}
if (fs.existsSync('../../openapi/mapcms/mapcms-api.yaml')) {
  removeDirectory('./generated/samrad-api');
  generateOpenApi('../../openapi/mapcms/mapcms-api.yaml','./generated/samrad-api');
}
if (fs.existsSync('../../openapi/mapcms/admin-api.yaml')) {
  generateOpenApi('../../openapi/mapcms/admin-api.yaml','./generated/samrad-api/admin-api');
}
