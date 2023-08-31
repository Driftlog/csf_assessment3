import { ActivatedRoute, Router } from '@angular/router';
import { Location } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { News } from 'src/app/models/news';
import { Publishednews } from 'src/app/models/publishednews';
import { NewsService } from 'src/app/news.service';

@Component({
  selector: 'app-newslist',
  templateUrl: './newslist.component.html',
  styleUrls: ['./newslist.component.css']
})
export class NewslistComponent implements OnInit{

  tag !: string
  location = inject(Location)
  newsList !: Publishednews[]
  router = inject(Router)
  newsSvc = inject(NewsService)
  activatedRoute = inject(ActivatedRoute)


  ngOnInit() {
    const time = this.activatedRoute.snapshot.params['time']
    const tag = this.activatedRoute.snapshot.params['tag']
    this.newsSvc.getNews(time, tag).then(value => this.newsList = value)
  }

  goBack() {
    this.router.navigate(['/']);
  }

  postNews() {
    this.router.navigate(['/postnews'])
  }

}
