import { TestBed } from "@angular/core/testing";
import { ApolloTestingController, ApolloTestingModule } from "apollo-angular/testing";
import { TestGraphqlService } from "../../../test/test-graphql.service";
import { ProductsDocument, ProductsQuery, ProductVariantsByProductIdDocument, ProductVariantsByProductIdQuery } from "./product-form.shop.generated";

const testProducts = {
  search: {
    items: {
      productVariantName: "Test",
      productVariantId: "1",
      productId: "1",
      productName: "Test",
      description: "Test test test",
      productAsset: {
        preview: ""
      }
    }
  }
};

const testProductVariants = {
  product: {
    id: "1",
    productVariants: {
      id: "Test",
      name: "1",
      options: {
        groupId: "1",
        id: "1",
        name: "Test",
        code: "test"
      }
    }
  }
};

describe("ProductFormGraphql", () => {
  let controller: ApolloTestingController;
  let graphqlService: TestGraphqlService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ApolloTestingModule]
    });
    controller = TestBed.inject(ApolloTestingController);
    graphqlService = TestBed.inject(TestGraphqlService);
  });

  afterEach(() => {
    controller.verify();
  });

  it("Ska kunna hämta produkter från Vendure", (done) => {
    graphqlService.query<ProductsQuery>(ProductsDocument).subscribe(res => {
      expect(res).toEqual(testProducts);
      done();
    });

    const op = controller.expectOne(ProductsDocument);

    expect(op.operation.operationName).toBe("products");

    op.flush({
      data: testProducts
    });

    controller.verify();
  });

  it("Ska kunna hämta produktvarianter med produktid från Vendure", (done) => {
    graphqlService.query<ProductVariantsByProductIdQuery>(ProductVariantsByProductIdDocument, { id: "1" }).subscribe(res => {
      expect(res).toEqual(testProductVariants);
      done();
    });

    const op = controller.expectOne(ProductVariantsByProductIdDocument);

    expect(op.operation.operationName).toBe("productVariantsByProductId");
    expect(op.operation.variables.id).toBe("1");

    op.flush({
      data: testProductVariants
    });

    controller.verify();
  });
});
