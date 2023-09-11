export class User {
  _id: string;
  firstName: string;
  lastName: string;
  email: string;
  schoolIds: any[];
	permissions: string[];
  menuIds: any[];
  password: string;

  constructor() {
    this.menuIds = new Array();
  }

  setUserFromAuthPic(user) {
    this._id = user._id;
    this.firstName = user.firstName;
    this.lastName = user.lastName;
    this.email = user.email;
    this.schoolIds = user.schoolIds;
    this.permissions = user.permissions;
    if(user.menuId) {
      this.menuIds = user.menuId;
    }
  }

}
