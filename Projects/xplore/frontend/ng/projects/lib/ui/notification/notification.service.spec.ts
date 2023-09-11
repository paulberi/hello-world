import { XpNotificationService } from "./notification.service";

describe("XpNotificationService", () => {
  let service: XpNotificationService;
  const mockNotyf: any = {
    error: jest.fn(),
    success: jest.fn(),
  };
  
  beforeEach(() => {
    service = new XpNotificationService(mockNotyf);    
    jest.clearAllMocks();
  });

  it("Ska kunna skapas", () => {    
    expect(service).toBeTruthy();
  });

  it("Ska kunna visa notifikation för ett fel", () => {
    const errorMessage = "Oops det här gick fel";

    service.error(errorMessage);
    
    expect(mockNotyf.error).toHaveBeenCalledWith(errorMessage);
  });

  it("Ska kunna visa notifikation för en framgång", () => {
    const successMessage = "Gick ju galant!";   

    service.success(successMessage);
    
    expect(mockNotyf.success).toHaveBeenCalledWith(successMessage);
  });
});
