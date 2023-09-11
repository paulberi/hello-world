import { Component, OnChanges, OnInit, SimpleChanges, Input} from '@angular/core';
import { FormBuilder, FormControl, FormGroup, UntypedFormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { XpDropzoneComponent } from '../../../../../lib/ui/dropzone/dropzone.component';
import { Bild } from '../../models/bild';
import { Samrad } from '../../models/samrad';
import { SamradStatus } from '../../models/samradStatus';
import { InitieraKundService } from '../../services/initiera-kund.service';
import { SamradService } from '../../services/samrad-admin.service';

@Component({
  selector: 'sr-skapa-samrad',
  templateUrl: './skapa-samrad.component.html',
  styleUrls: ['./skapa-samrad.component.scss']
})
export class SkapaSamradComponent implements OnInit, OnChanges{


  fileList:any[];

  @Input() samrad:Samrad;

  @Input() editMode = false;

  newSamrad: Samrad;

  bildList:Bild[];

  bild:Bild;

  selectedFile:File;

  form:FormGroup;

  subscription:Subscription[];

  uploadedFilename:string;

  ingenNamn = false;

  isDisabled=true;

  uploadHeader=false;

  cannotBeDeleted=false;

  deleteSamradWarning=false;

  kundId = "b6b20dd8-5692-4871-89e1-0fa36b397800";


  constructor(
    private formBuilder:FormBuilder,
    private samradService:SamradService,
    private router:Router,
    private initieraKundService: InitieraKundService
    ) {}


  ngOnInit(): void {
    this.fileList=[];
    this.bildList=[]
    this.subscription=[];
    this.initializeForm(this.samrad);
    this.bild=new Bild();

  }

  ngOnChanges(changes: SimpleChanges): void {
      if(changes.samrad) {
        this.initializeForm(this.samrad);
        this.canSave();
      }
  }

  initializeForm(samrad: Samrad){
    this.form=this.formBuilder.group({
      namn:[samrad?.namn, Validators.required],
      ingress:[samrad?.ingress],
      brodtext:[samrad?.brodtext],
      picture:[samrad?.bildList]
    });
    this.form.valueChanges.subscribe((value) => {
      this.canSave();
    });
  }

  canSave() {
    if(this.form.valid){
      this.isDisabled = false;
    }else{
      this.isDisabled=true;
    }
  }

  //bilder uppladdning är bortkoplat från skappa anrop för tillfälg


  submit() {
    this.newSamrad=new Samrad();
    this.form.value;
    this.newSamrad.namn=this.form.get("namn").value;
    this.newSamrad.ingress=this.form.get("ingress").value;
    this.newSamrad.brodtext=this.form.get("brodtext").value;
    this.newSamrad.status=SamradStatus.UTKAST;
    this.fileList.forEach((file)=>{
      this.bild= new Bild();
      this.bild.namn=file.name;
      this.bild.bildId="123"
      this.bild.typ=file.type;
      this.bild.bild=file;
      this.bildList.push(this.bild);
    });
    this.newSamrad.bildList=this.fileList;
      //hård kodad Exemple kundId
      if(!this.editMode) {
        this.samradService.saveSamrad$(this.initieraKundService.kundInfo.id, this.newSamrad).subscribe();
      } else {
        this.newSamrad.bildList=null;
        this.newSamrad.samradId = this.samrad.samradId;
        this.samradService.updateSamrad$(this.initieraKundService.kundInfo.id, this.samrad.samradId, this.newSamrad).subscribe();
      }

      this.router.navigate(["admin"]);
  }

  onDelete() {
    if(this.samrad.status==SamradStatus.ARKIVERAD || this.samrad.status==SamradStatus.PUBLICERAD || this.samrad.status== SamradStatus.AVPUBLICERAD){
      this.cannotBeDeleted=true;
    }else{
      this.deleteSamradWarning=true;
    }
  }

  deleteSamrad() {
   this.samradService.deleteSamrad$(this.initieraKundService.kundInfo.id, this.samrad.samradId).subscribe();
   this.router.navigate(["admin"]);
 }



  onFileChanged(event){
    this.selectedFile=event.target.files[0];
    this.bild.namn=this.selectedFile.name;
    this.bild.typ=this.selectedFile.type;
    this.uploadedFilename=this.selectedFile.name;
    this.fileList.push(this.selectedFile);
    this.uploadHeader=true;
/*     this.samradService.saveBild("2dc4bbd1-a48a-4c6f-b82f-aaa85b8eda5d",this.selectedFile).subscribe((data:Bild)=>{
      console.log(data)
      let list:Samrad[]=[];
    }); */
  }


  angraUppladdning(event){
    let fileName=event.path[0].innerHTML;
    this.fileList.forEach((file)=>{
      if(file.name===fileName){
        this.fileList=this.fileList.filter(file1=> file1!== file);
      }
    })
  }


  returnToAdmin(){
    this.router.navigate(["admin"]);
  }

  onClose(event:any){
    this.cannotBeDeleted=false;
    this.deleteSamradWarning=false;
  }
  onAction(event:any){
    this.deleteSamradWarning=false;
    this.deleteSamrad();
  }
  avpubliceraSamrad(){
    this.samrad.status = SamradStatus.AVPUBLICERAD
    this.router.navigate(["admin"]);
  }
}


