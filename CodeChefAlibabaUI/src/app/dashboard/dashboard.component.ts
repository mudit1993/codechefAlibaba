import { Component, OnInit } from '@angular/core';
import { AnnualReportService } from './report.service'

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  reports: Array<any>;
  columns: string[];
  yearList: string[];
  selectedYear: string;
  sortAvgScore: Boolean;
  constructor(private atService: AnnualReportService) { }

  ngOnInit() {
    this.sortAvgScore = false;
    this.yearList = this.atService.getYearList();
    this.selectedYear = this.yearList[0];
    this.columns = this.atService.getColumns();
    this.atService.getReports(this.selectedYear, this.sortAvgScore).subscribe((response) => {
      this.reports = response as Array<any>;
    });

  }
  selectYear(yearSelected: string) {
    this.selectedYear = yearSelected;
    this.atService.getReports(this.selectedYear, this.sortAvgScore).subscribe((response) => {
      this.reports = response as Array<any>;
    });
  }
  onViewRankList() {
    this.sortAvgScore = !this.sortAvgScore;
    this.atService.getReports(this.selectedYear, this.sortAvgScore).subscribe((response) => {
      this.reports = response as Array<any>;
    });
  }
  onFetchData() {
    this.atService.populateReport(this.selectedYear).subscribe((response) => {
      this.reports = response as Array<any>;
    });
  }

}
