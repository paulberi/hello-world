import random
import xml.etree.ElementTree as ET

class XmlUtil:

    def clone_namespace(self, sourcePath, targetPath, name):
        tree = ET.parse(sourcePath + "/namespace.xml")  
        root = tree.getroot()
        idElem = root.find("id")
        idElem.text = self.generateId(self.getIdPrefix(idElem.text))
        prefixElem = root.find("prefix")
        prefixElem.text = name
        uriElem = root.find("uri")
        uriElem.text = "http://www." + name + ".se"
        self.save_file(root, targetPath + "/namespace.xml")
        return idElem.text

    def clone_workspace(self, sourcePath, targetPath, name):
        tree = ET.parse(sourcePath + "/workspace.xml")  
        root = tree.getroot()
        idElem = root.find("id")
        idElem.text = self.generateId(self.getIdPrefix(idElem.text))
        nameElem = root.find("name")
        nameElem.text = name
        self.save_file(root, targetPath + "/workspace.xml")
        return idElem.text

    def clone_datastore(self, sourcePath, targetPath, name, nameExt, workspaceId):
        tree = ET.parse(sourcePath + "/datastore.xml")  
        root = tree.getroot()
        idElem = root.find("id")
        idElem.text = self.generateId(self.getIdPrefix(idElem.text))
        nameElem = root.find("name")
        nameElem.text = nameExt
        descriptionElem = root.find("description")
        descriptionElem.text = name.capitalize() + " kommunkarta"
        workspaceElem = root.find("workspace")
        workspaceIdElem = workspaceElem.find("id")
        workspaceIdElem.text = workspaceId
        connectionParamsElem = root.find("connectionParameters")
        for entryElem in connectionParamsElem.getchildren():
            if entryElem.attrib.values()[0] == "schema":
                entryElem.text = nameExt
            elif entryElem.attrib.values()[0] == "namespace":
                entryElem.text = "http://www." + nameExt + ".se"
        self.save_file(root, targetPath + "/datastore.xml")
        return idElem.text

    def clone_featuretype(self, sourcePath, targetPath, namespaceId, datastoreId):
        tree = ET.parse(sourcePath + "/featuretype.xml")  
        root = tree.getroot()
        idElem = root.find("id")
        idElem.text = self.generateId(self.getIdPrefix(idElem.text))
        namespaceElem = root.find("namespace")
        namespaceIdElem = namespaceElem.find("id")
        namespaceIdElem.text = namespaceId
        datastoreElem = root.find("store")
        datastoreIdElem = datastoreElem.find("id")
        datastoreIdElem.text = datastoreId
        self.save_file(root, targetPath + "/featuretype.xml")
        return idElem.text

    def clone_layer(self, sourcePath, targetPath, featureTypeId):
        tree = ET.parse(sourcePath + "/layer.xml")  
        root = tree.getroot()
        idElem = root.find("id")
        idElem.text = self.generateId(self.getIdPrefix(idElem.text))
        featureTypeElem = root.find("resource")
        featureTypeIdElem = featureTypeElem.find("id")
        featureTypeIdElem.text = featureTypeId
        self.save_file(root, targetPath + "/layer.xml")
        return idElem.text

    def save_file(self, root, targetPath):
        xmldata = ET.tostring(root)  
        xmlfile = open(targetPath, "w")
        xmlfile.write(xmldata)

    def getIdPrefix(self, id):
        return id.split("-")[0]

    def generateId(self, prefix):
        return prefix + "-" + self.randomHex(8) + ":" + self.randomHex(11) + ":-" + self.randomHex(4)      

    def randomHex(self, count):
        result = ""
        for i in range(count):
            result += hex(random.randint(0, 16))[2]
        return result