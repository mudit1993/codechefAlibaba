import { Injectable } from '@angular/core';
import { Observable } from 'rxjs-compat/Observable';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import 'rxjs/add/observable/of';
import 'rxjs/add/operator/delay';
//import { REPORTS } from './mock-data';
@Injectable()
export class AnnualReportService {
    constructor(private http: HttpClient) { }
    getReports(selectedYear: string, sortAvg: Boolean): Observable<any> {
        let url = "http://149.129.138.193:8080/CodechefAlibaba-1.0.0/reports?year=" + selectedYear + "&sortAvg=" + sortAvg;
        return this.http.get(url);
        //        return Observable.of(REPORTS).delay(100);
    }
    populateReport(selectedYear: string): Observable<any> {
        let url = "http://149.129.138.193:8080/CodechefAlibaba-1.0.0/fetchReports?year=" + selectedYear;
        return this.http.get(url);
    }
    getColumns(): string[] {
        return ["#", "User Name", "Country", "Score", "Contests", "Average Score"];
    };
    getYearList(): string[] {
        return ["2018", "2017", "2016", "2015", "2014", "2013", "2012"];
    }
}