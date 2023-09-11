import  Swal  from 'sweetalert2';

export class Alert {

  showAlert(title : string, text : string, icon : any) {
    Swal.fire({
      title: title,
      text: text,
      icon: icon,
      confirmButtonColor: '#063752'
    })
  }

  showAdvancedAlert(title : string, text : string, icon : any, confirmText : string, cancelText : string) : any {
    return Swal.fire({
      title: title,
      text: text,
      icon: icon,
      showCancelButton: true,
      confirmButtonText: confirmText,
      cancelButtonText: cancelText,
      confirmButtonColor: "#063752",
      cancelButtonColor: "#063752"
    })
  }

  showAlertAndUpdatePage(title : string, text : string, icon : any) {
    Swal.fire({
      title: title,
      text: text,
      icon: icon,
      confirmButtonColor: '#063752'
    }).then((result) => {
      if (result.isConfirmed) {
        window.location.reload();
      }
    })
  }

}
