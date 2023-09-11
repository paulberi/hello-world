export const testParentForm = {
  customerForm: {
    orderId: 123,
    shippingAddress: "test@metria.se"
  },
  productForm: {
    productId: "1",
    productVariantId: "1"
  },
  dynamicForm: {
    format: "Geopackage"
  }
};

export const testEmptyParentForm = {
  customerForm: {
    orderId: 123,
    shippingAddress: "test@metria.se"
  },
  productForm: null,
  dynamicForm: null
};

export const testInvalidGeometry = {
  type: "FeatureCollection",
  features: [
    {
      type: "Feature",
      properties: {},
      geometry: {
        type: "LineString",
        coordinates: [
          [
            17.3279070854187,
            62.94182915886935
          ],
          [
            17.332359552383423,
            62.94229768240814
          ]
        ]
      }
    }
  ]
};

export const testValidGeometry = {
  type: "FeatureCollection",
  features: [
    {
      type: "Feature",
      properties: {},
      geometry: {
        type: "Polygon",
        coordinates: [
          [
            17.3279070854187,
            62.94182915886935
          ],
          [
            17.332359552383423,
            62.94229768240814
          ]
        ]
      }
    }
  ]
};

export const testNoGeometry = {
  type: "FeatureCollection",
  features: []
};

interface MockFile {
  name: string;
  body: string;
  mimeType: string;
}

const createFileFromMockFile = (file: MockFile): File => {
  const blob = new Blob([file.body], { type: file.mimeType }) as any;
  blob['lastModifiedDate'] = new Date();
  blob['name'] = file.name;
  return blob as File;
};

export const createMockFileList = (files: MockFile[]) => {
  const fileList: FileList = {
      length: files.length,
      item(index: number): File {
          return fileList[index];
      }
  };
  files.forEach((file, index) => fileList[index] = createFileFromMockFile(file));

  return fileList;
};

