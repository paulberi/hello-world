#coding=UTF-8

import os
import sys
import XmlUtil

# Remoted debug module
#import ptvsd

# Enable remoted debugging
#ptvsd.enable_attach()
#ptvsd.break_into_debugger()

xmlUtil = XmlUtil.XmlUtil()

if __name__ == '__main__':
    reload(sys)
    sys.setdefaultencoding('utf-8')

    if len(sys.argv) < 4:
        print("*\n* Usage: python create_new_customer.py <source_workspace> <target_workspace> <organisation>" +
              "\n*\n*   source_workspace = Det existerande workspacet, ex. 'norra'" +
              "\n*   target_workspace = Det nya workspacet utan svenska tecken, ex. 'sodra'" +
              "\n*   organisation     = Organisationsnamnet som används i databasen, ex. Södra" +
              "\n*\n* Ex. python create_new_customer.py norra sodra Södra\n*")
    else:
        source = sys.argv[1]
        target = sys.argv[2]
        org_name = sys.argv[3]
        basePath = "../../../geoserver/config/utv/geoserver_data_dir"

        workspacePath = basePath + "/workspaces"

        sourcePath = workspacePath + "/" + source
        targetPath = workspacePath + "/" + target
        os.mkdir(targetPath)
        namespaceId = xmlUtil.clone_namespace(sourcePath, targetPath, target)
        workspaceId = xmlUtil.clone_workspace(sourcePath, targetPath, target)

        sourcePathDS = sourcePath + "/" + source
        targetPathDS = targetPath + "/" + target
        os.mkdir(targetPathDS)
        datastoreId = xmlUtil.clone_datastore(sourcePathDS, targetPathDS, target, target, workspaceId)

        for layerPath in os.listdir(sourcePathDS):
            if layerPath.find(".") == -1:
                sourcePathLayer = sourcePathDS + "/" + layerPath
                targetPathLayer = targetPathDS + "/" + layerPath
                os.mkdir(targetPathLayer)
                featureTypeId = xmlUtil.clone_featuretype(sourcePathLayer, targetPathLayer, namespaceId, datastoreId, org_name)
                xmlUtil.clone_layer(sourcePathLayer, targetPathLayer, featureTypeId)


        securityPath = basePath + "/security"
        sourcePathRoles = securityPath + "/role/default/roles.xml"
        sourcePathUsers = securityPath + "/usergroup/default/users.xml"
        sourcePathLayers = securityPath + "/layers.properties"

        xmlUtil.extendRoles(sourcePathRoles, target)
        xmlUtil.extendUsers(sourcePathUsers, target)
        xmlUtil.extendLayers(sourcePathLayers, target)

        print("Forestlink workspace '" + target + "' skapat.")

