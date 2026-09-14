import React, { useState } from 'react';
import { reportService } from '../services/reportService';
import Toast from '../components/Toast';
import { FileText, FileSpreadsheet, Download, Sparkles } from 'lucide-react';

const Reports = () => {
  const [toast, setToast] = useState(null);
  const [downloading, setDownloading] = useState(null);

  const handleDownloadPdf = async () => {
    setDownloading('pdf');
    try {
      await reportService.downloadCirculationPdf();
      setToast({ message: 'Circulation PDF Report downloaded successfully!', type: 'success' });
    } catch (err) {
      setToast({ message: 'Failed to export PDF report', type: 'error' });
    } finally {
      setDownloading(null);
    }
  };

  const handleDownloadInventoryCsv = async () => {
    setDownloading('inventory');
    try {
      await reportService.downloadInventoryCsv();
      setToast({ message: 'Inventory CSV Report downloaded successfully!', type: 'success' });
    } catch (err) {
      setToast({ message: 'Failed to export CSV report', type: 'error' });
    } finally {
      setDownloading(null);
    }
  };

  const handleDownloadOverdueCsv = async () => {
    setDownloading('overdue');
    try {
      await reportService.downloadOverdueCsv();
      setToast({ message: 'Overdue Fines CSV Report downloaded successfully!', type: 'success' });
    } catch (err) {
      setToast({ message: 'Failed to export CSV report', type: 'error' });
    } finally {
      setDownloading(null);
    }
  };

  return (
    <div className="space-y-6 animate-fade-in">
      {toast && <Toast {...toast} onClose={() => setToast(null)} />}

      <div>
        <h1 className="text-2xl font-extrabold text-slate-100 tracking-tight">System Export & Reports Hub</h1>
        <p className="text-xs text-slate-400 mt-1">Generate official PDF and CSV reports for circulation history, catalog inventory, and overdue fine logs.</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        {/* PDF Circulation Report */}
        <div className="glass-panel p-6 rounded-3xl border border-slate-800 flex flex-col justify-between hover:border-indigo-500/40 transition-all shadow-xl">
          <div>
            <div className="p-3 rounded-2xl bg-indigo-500/10 border border-indigo-500/20 text-indigo-400 w-fit mb-4">
              <FileText className="w-6 h-6" />
            </div>
            <h3 className="text-base font-bold text-slate-100">Circulation Stats Report (PDF)</h3>
            <p className="text-xs text-slate-400 mt-2 leading-relaxed">
              Official formatted PDF document detailing all member borrowings, issue dates, due dates, and return statuses.
            </p>
          </div>

          <button
            onClick={handleDownloadPdf}
            disabled={downloading === 'pdf'}
            className="w-full mt-6 py-2.5 bg-indigo-600 hover:bg-indigo-500 text-white font-semibold text-xs rounded-xl shadow-lg shadow-indigo-600/30 transition-all flex items-center justify-center gap-2"
          >
            <Download className="w-4 h-4" />
            {downloading === 'pdf' ? 'Generating PDF...' : 'Download PDF Report'}
          </button>
        </div>

        {/* CSV Catalog Inventory Report */}
        <div className="glass-panel p-6 rounded-3xl border border-slate-800 flex flex-col justify-between hover:border-purple-500/40 transition-all shadow-xl">
          <div>
            <div className="p-3 rounded-2xl bg-purple-500/10 border border-purple-500/20 text-purple-400 w-fit mb-4">
              <FileSpreadsheet className="w-6 h-6" />
            </div>
            <h3 className="text-base font-bold text-slate-100">Book Inventory Master (CSV)</h3>
            <p className="text-xs text-slate-400 mt-2 leading-relaxed">
              Export complete list of all catalog titles, authors, categories, ISBNs, total copy counts, and current availability status.
            </p>
          </div>

          <button
            onClick={handleDownloadInventoryCsv}
            disabled={downloading === 'inventory'}
            className="w-full mt-6 py-2.5 bg-purple-600 hover:bg-purple-500 text-white font-semibold text-xs rounded-xl shadow-lg shadow-purple-600/30 transition-all flex items-center justify-center gap-2"
          >
            <Download className="w-4 h-4" />
            {downloading === 'inventory' ? 'Generating CSV...' : 'Download Inventory CSV'}
          </button>
        </div>

        {/* CSV Overdue Fines Report */}
        <div className="glass-panel p-6 rounded-3xl border border-slate-800 flex flex-col justify-between hover:border-rose-500/40 transition-all shadow-xl">
          <div>
            <div className="p-3 rounded-2xl bg-rose-500/10 border border-rose-500/20 text-rose-400 w-fit mb-4">
              <FileSpreadsheet className="w-6 h-6" />
            </div>
            <h3 className="text-base font-bold text-slate-100">Overdue Fines Log (CSV)</h3>
            <p className="text-xs text-slate-400 mt-2 leading-relaxed">
              Export itemized list of all accrued overdue fines, member contact details, fine amounts, and payment status.
            </p>
          </div>

          <button
            onClick={handleDownloadOverdueCsv}
            disabled={downloading === 'overdue'}
            className="w-full mt-6 py-2.5 bg-rose-600 hover:bg-rose-500 text-white font-semibold text-xs rounded-xl shadow-lg shadow-rose-600/30 transition-all flex items-center justify-center gap-2"
          >
            <Download className="w-4 h-4" />
            {downloading === 'overdue' ? 'Generating CSV...' : 'Download Overdue CSV'}
          </button>
        </div>
      </div>
    </div>
  );
};

export default Reports;
