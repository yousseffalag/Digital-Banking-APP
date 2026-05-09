import { Component, OnInit } from '@angular/core';
import {DashboardService} from "../services/dashboard.service";
import {ChartData, ChartOptions} from "chart.js";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  stats: any;
  
  // Main Statistics Chart (Bar)
  chartData: ChartData<'bar'> = {
    labels: ['Customers', 'Accounts', 'Transactions'],
    datasets: [
      { 
        data: [0, 0, 0], 
        label: 'Platform Usage',
        backgroundColor: '#4e73df',
        borderRadius: 5
      }
    ]
  };

  // Distribution Chart (Doughnut)
  doughnutChartData: ChartData<'doughnut'> = {
    labels: ['Customers', 'Accounts', 'Transactions'],
    datasets: [
      { 
        data: [0, 0, 0],
        backgroundColor: ['#4e73df', '#1cc88a', '#36b9cc'],
        hoverOffset: 4
      }
    ]
  };

  // Growth Chart (Line - Simulated)
  lineChartData: ChartData<'line'> = {
    labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
    datasets: [
      { 
        data: [10000, 25000, 15000, 45000, 32000, 60000], 
        label: 'Total Assets Growth',
        fill: true,
        borderColor: '#4e73df',
        tension: 0.4,
        backgroundColor: 'rgba(78, 115, 223, 0.05)'
      }
    ]
  };

  chartOptions: ChartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: { display: false }
    },
    scales: {
      y: { beginAtZero: true, grid: { display: false } },
      x: { grid: { display: false } }
    }
  };

  doughnutOptions: ChartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: { position: 'bottom' }
    }
  };

  constructor(private dashboardService: DashboardService) { }

  ngOnInit(): void {
    this.dashboardService.getStatistics().subscribe({
      next: (data) => {
        this.stats = data;
        
        const metrics = [data.totalCustomers, data.totalAccounts, data.totalOperations];
        
        this.chartData.datasets[0].data = metrics;
        this.doughnutChartData.datasets[0].data = metrics;
      },
      error: (err) => {
        console.log(err);
      }
    });
  }
}
