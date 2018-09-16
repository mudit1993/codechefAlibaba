import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule }    from '@angular/forms';
import {RouterModule} from '@angular/router';
import { AppComponent } from './app.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { FormsModule } from '@angular/forms';
import { AnnualReportService } from './dashboard/report.service';
import { HttpClient, HttpClientModule } from '@angular/common/http';


@NgModule({
  declarations: [
    AppComponent,
    DashboardComponent
  ],
  imports: [
    HttpClientModule,
    FormsModule,
    BrowserModule,
    ReactiveFormsModule,
    RouterModule.forRoot([{ path: '', component: DashboardComponent },
    // otherwise redirect to home
    { path: '**', redirectTo: '', pathMatch:'full' }], {useHash:true})
  ],
  providers: [AnnualReportService,HttpClient],
  bootstrap: [AppComponent]
})
export class AppModule { }
