import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {ScoresComponent} from "./components/scores/scores.component";
import {AppComponent} from "./app/app.component";
import {CommonModule} from "@angular/common";
import {HomeComponent} from "./components/home/home.component";

const routes: Routes = [
  // { path: '', component: AppComponent },
  { path: 'scores', component: ScoresComponent },
  { path : 'home', component: HomeComponent},
  { path : '', component: HomeComponent},
  // { path: 'calendar', component: CalendarComponent}
];
@NgModule({
  imports: [
    RouterModule.forRoot(routes),
    CommonModule],
  exports: [RouterModule]
})
export class AppRoutingModule { }
