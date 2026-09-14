import API from './api';

export const reportService = {
  downloadCirculationPdf: async () => {
    const response = await API.get('/reports/circulation/pdf', { responseType: 'blob' });
    const url = window.URL.createObjectURL(new Blob([response.data], { type: 'application/pdf' }));
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', 'circulation_report.pdf');
    document.body.appendChild(link);
    link.click();
    link.remove();
  },

  downloadInventoryCsv: async () => {
    const response = await API.get('/reports/inventory/csv', { responseType: 'blob' });
    const url = window.URL.createObjectURL(new Blob([response.data], { type: 'text/csv' }));
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', 'inventory_report.csv');
    document.body.appendChild(link);
    link.click();
    link.remove();
  },

  downloadOverdueCsv: async () => {
    const response = await API.get('/reports/overdue/csv', { responseType: 'blob' });
    const url = window.URL.createObjectURL(new Blob([response.data], { type: 'text/csv' }));
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', 'overdue_fines_report.csv');
    document.body.appendChild(link);
    link.click();
    link.remove();
  }
};
