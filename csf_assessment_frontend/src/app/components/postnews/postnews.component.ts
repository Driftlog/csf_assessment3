import { Component, ElementRef, OnInit, ViewChild, inject } from '@angular/core';
import { Location } from '@angular/common';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { News } from 'src/app/models/news';
import { NewsService } from 'src/app/news.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-postnews',
  templateUrl: './postnews.component.html',
  styleUrls: ['./postnews.component.css']
})
export class PostnewsComponent implements OnInit{

  @ViewChild('image') image !: ElementRef

  location = inject(Location)
  fb = inject(FormBuilder)
  newsSvc = inject(NewsService)
  router = inject(Router)

  tagsList : string[] = []
  news !: FormGroup


  ngOnInit(){
    this.news = this.createForm()
  }

  previousPage() {
    this.location.back()
  }

  createForm() {
    return this.fb.group({
      title: this.fb.control<string>('', [ Validators.required, Validators.min(5) ]),
      description: this.fb.control<string>('', [ Validators.required, Validators.min(5)]),
    })

  }

  processNews() {
    const news = <News> this.news.value

    const formData = new FormData()
    formData.set('image', this.image.nativeElement.files[0])
    formData.set("description", news.description)
    formData.set("title", news.title)
    if (this.tagsList.length > 0) {
    formData.set('tags', JSON.stringify(this.tagsList))}
    else {
      formData.set('tags', JSON.stringify([]))
    }
    this.newsSvc.postNews(formData).then((result) => {
        alert("result")
        this.router.navigate([''])
    }).catch((error) => {
      alert(error.toString())
    })
  }

  fileEmpty() {
    if (this.image) {
      return true
    }
    return false;
  }

  addTag(tag : string) {

    const tagArr = tag.split(" ")

    tagArr.forEach(tag => {

    if (!this.tagsList.includes(tag)) {
      this.tagsList.push(tag)
    }
    })

  }

  deleteTag(tag : string) {
    const idx = this.tagsList.indexOf("tag")
    this.tagsList.splice(idx)
  }

}
