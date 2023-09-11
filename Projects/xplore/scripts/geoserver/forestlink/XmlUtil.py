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

    def clone_featuretype(self, sourcePath, targetPath, namespaceId, datastoreId, name):
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
        cqlFilterElem = root.find("cqlFilter")
        cqlFilterElem.text = "organisation='" + name + "'"
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

    def extendRoles(self, sourcePath, name):
        ET.register_namespace("", "http://www.geoserver.org/security/roles")
        ns = {"ns": "http://www.geoserver.org/security/roles"}
        tree = ET.parse(sourcePath)
        root = tree.getroot()

        readRole = name.upper() + "_READ"

        roleListElem = root.find("ns:roleList", ns)
        roleElem = ET.Element("role")
        roleElem.set("id", readRole)
        roleElem.tail = "\n";
        roleListElem.append(roleElem)

        userListElem = root.find("ns:userList", ns)

        userRolesElem = ET.Element("userRoles")
        userRolesElem.set("username", name)
        userRolesElem.tail = "\n"
        userRolesElem.text = "\n"

        roleRefElem = ET.Element("roleRef")
        roleRefElem.set("roleID", readRole)
        roleRefElem.tail = "\n"

        userRolesElem.append(roleRefElem)
        userListElem.append(userRolesElem)

        self.save_file_with_header(root, sourcePath)

    def extendUsers(self, sourcePath, name):
        ET.register_namespace("", "http://www.geoserver.org/security/users")
        ns = {"ns": "http://www.geoserver.org/security/users"}
        tree = ET.parse(sourcePath)
        root = tree.getroot()

        usersElem = root.find("ns:users", ns)

        userElem = ET.Element("user")
        userElem.set("enabled", "true")
        userElem.set("name", name)
        userElem.set("password", "crypt1:rZErwwDY8+yNAcb/42cuew==") #sommar19
        userElem.tail = "\n";

        usersElem.append(userElem)

        self.save_file_with_header(root, sourcePath)

    def extendLayers(self, sourcePath, name):
        f = open(sourcePath, "r")
        contents = f.readlines()
        f.close()

        contents.insert(6, name + ".*.r=" + name.upper() + "_READ\n")

        f = open(sourcePath, "w")
        f.write("".join(contents))
        f.close()

    def save_file(self, root, targetPath):
        xmldata = ET.tostring(root,encoding='utf-8')
        xmlfile = open(targetPath, "w")
        xmlfile.write(xmldata)

    def save_file_with_header(self, root, targetPath):
        header = '<?xml version="1.0" encoding="UTF-8" standalone="no"?>\n'
        xmldata = header + ET.tostring(root)
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