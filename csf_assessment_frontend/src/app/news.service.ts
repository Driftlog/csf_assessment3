import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import {firstValueFrom, lastValueFrom} from 'rxjs'
import { Toptags } from './models/toptags';
import { Publishednews } from './models/publishednews';

@Injectable({
  providedIn: 'root'
})
export class NewsService {

  http = inject(HttpClient)

  constructor() { }

  postNews(formData : FormData) {
    return firstValueFrom(this.http.post("/postnews", formData))
  }

  getTags(time : number) : Promise<Toptags[]> {

    const params = new HttpParams()
              .append("time", time)

    return lastValueFrom<any>(this.http.get("/getTags", {params}))
  }

  getNews(time: number, tag : string) : Promise<Publishednews[]> {

    const params = new HttpParams()
                        .append("time", time)
                        .append("tag", tag)

    return lastValueFrom<any>(this.http.get("/getnews", {params}))
  }
}
