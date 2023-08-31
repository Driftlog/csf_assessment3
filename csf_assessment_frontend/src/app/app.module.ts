import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { HomepageComponent } from './components/homepage/homepage.component';
import { PostnewsComponent } from './components/postnews/postnews.component';
import { NewslistComponent } from './components/newslist/newslist.component';
import { RouterModule, Routes } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import {HttpClientModule} from '@angular/common/http'

const routes : Routes = [
  {path: '', component: HomepageComponent },
  {path: 'postnews', component: PostnewsComponent},
  {path: 'newslist', component: NewslistComponent}
]

@NgModule({
  declarations: [
    AppComponent,
    HomepageComponent,
    PostnewsComponent,
    NewslistComponent
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot(routes, {useHash: true} ),
    ReactiveFormsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
