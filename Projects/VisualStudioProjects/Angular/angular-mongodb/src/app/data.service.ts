import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Movie } from './models/movie.model';
import { Observable }   from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DataService {

  constructor(private http: HttpClient) { }

  getMovieListing(): Observable<Movie[]> {
    return this.http.get<Movie[]>('http://starlord.hackerearth.com/movieslisting');
  }
}