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
    if len(sys.argv) < 3:
        print("Usage: python clone_workspace.py <kommun_source> <kommun_target>")
    else:
        source = sys.argv[1]
        target = sys.argv[2]
        sourceExt = source + "ext"
        targetExt = target + "ext"
        basePath = "../../../geoserver/config/utv/geoserver_data_dir/workspaces"

        sourcePathExt = basePath + "/" + sourceExt
        targetPathExt = basePath + "/" + targetExt
        os.mkdir(targetPathExt)
        namespaceExtId = xmlUtil.clone_namespace(sourcePathExt, targetPathExt, targetExt)
        workspaceExtId = xmlUtil.clone_workspace(sourcePathExt, targetPathExt, targetExt)

        sourcePathExtExt = sourcePathExt + "/" + sourceExt
        targetPathExtExt = targetPathExt + "/" + targetExt
        os.mkdir(targetPathExtExt)
        datastoreExtId = xmlUtil.clone_datastore(sourcePathExtExt, targetPathExtExt, target, targetExt, workspaceExtId)

        for layerPath in os.listdir(sourcePathExtExt):
            if layerPath.find(".") == -1:
                sourcePathLayer = sourcePathExtExt + "/" + layerPath
                targetPathLayer = targetPathExtExt + "/" + layerPath
                os.mkdir(targetPathLayer)
                featureTypeId = xmlUtil.clone_featuretype(sourcePathLayer, targetPathLayer, namespaceExtId, datastoreExtId)
                xmlUtil.clone_layer(sourcePathLayer, targetPathLayer, featureTypeId)

        print("Geoserver workspace '" + targetExt + "' skapat.")

