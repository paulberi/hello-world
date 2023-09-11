
import { MapsAPILoader } from '@agm/core';
import { Component, NgZone, OnInit} from '@angular/core';
import { Loader } from '@googlemaps/js-api-loader';
import { AgmCoreModule } from '@agm/core';

//https://www.freakyjolly.com/angular-google-maps-get-current-location-latitude-longitude-on-click-event/

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'Maps';
  latitude:number=-34.397;
  longitude:number=150.644;
  latitude1:number=63.8543515;
  longitude1:number=20.3281625;
  latitude2:number=63.99999;
  longitude2:number=20.34444;
  address!: string;
  markers=[];
  constructor(
    private mapsAPILoader: MapsAPILoader,
    private ngZone: NgZone
  ) { }

  ngOnInit(){
    this.setCurrentLocation1();
    this.mapsAPILoader.load().then(()=>{
      new google.maps.Map(document.getElementById('map') as HTMLElement,{
        center:{lat:-34.397, lng:150.644},zoom:2
      });
    })
  }
  private setCurrentLocation1(){
    navigator.geolocation.getCurrentPosition((position)=>{
      this.latitude=position.coords.latitude;
      this.longitude=position.coords.longitude;
      console.log(this.latitude+" and "+this.longitude);

      this.mapsAPILoader.load().then(()=>{
        console.log(this.latitude+" and "+this.longitude);
        new google.maps.Map(document.getElementById('testMap') as HTMLElement,{
          center:{lat:this.latitude, lng:this.longitude},zoom:8
        });
      })
    })
  }
  onMapClicked(event:any){
    console.table(event.coords);
    this.latitude = event.coords.lat;
    this.longitude = event.coords.lng;
  }
}
