import { NewsService } from './../../news.service';
import { Component, OnInit, SimpleChanges, inject } from '@angular/core';
import {Router} from '@angular/router';
import { Toptags } from 'src/app/models/toptags';

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.css']
})
export class HomepageComponent implements OnInit{

    interval : number = 5;
    intervalList : number[] = [5,15,30,45,60];
    tagsList!: Toptags[];
    router = inject(Router)
    newsSvc = inject(NewsService)

    ngOnInit() {
      this.displayTags(this.interval)
    }

    postNews() {
      this.router.navigate(['postnews'])
    }

    displayTags(interval : number) {
      this.newsSvc.getTags(interval).then(
        (value) => {
          value.forEach((x : Toptags) => {
            this.tagsList.push(x)})
          }
          )
      .catch((error) => {
          console.log(error)
      })
    }

    getTags(event : any) {
      this.interval = event.target.value
      this.newsSvc.getTags(this.interval).then((value) => {
        this.tagsList = value
      })
    }


}
