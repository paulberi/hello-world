import {Component, OnInit, ViewChild} from '@angular/core';
import {MatPaginator, MatTableDataSource, MatSort } from '@angular/material';
import { DataService } from '../data.service';
import { Movie } from '../models/movie.model';

@Component({
  selector: 'app-bodyarea',
  templateUrl: './bodyarea.component.html',
  styleUrls: ['./bodyarea.component.scss']
})
export class BodyareaComponent implements OnInit {

  displayedColumns: string[] = ['movie_title', 'title_year'];
  dataSource = new MatTableDataSource<Movie>();
  
  applyFilter(filterValue: string) {
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }
  
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private data: DataService) { 
    data.getMovieListing().subscribe((response) => {
      this.dataSource.data = response;
  });
  }

  ngOnInit(){
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }
}