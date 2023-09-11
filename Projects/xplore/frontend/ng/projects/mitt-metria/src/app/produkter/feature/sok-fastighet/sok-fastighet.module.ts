import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SokFastighetComponent } from './sok-fastighet.component';
import { SearchFieldComponent } from '../../../../../../lib/map/components/search-panel/search-field.component';
import { SearchTypeComponent } from '../../../../../../lib/map/components/search-panel/search-type.component';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatInputModule } from '@angular/material/input';
import { ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';

@NgModule({
  declarations: [SokFastighetComponent, SearchFieldComponent, SearchTypeComponent],
  imports: [
    CommonModule,
    MatFormFieldModule,
    MatIconModule,
    MatAutocompleteModule,
    ReactiveFormsModule,
    MatInputModule,
    MatButtonModule
  ],
  exports: [SokFastighetComponent]
})
export class MMSokFastighetModule { }
