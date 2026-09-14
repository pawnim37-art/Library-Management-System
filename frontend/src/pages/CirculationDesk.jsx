import React, { useState, useEffect, useCallback } from 'react';
import { circulationService } from '../services/circulationService';
import Modal from '../components/Modal';
import Toast from '../components/Toast';
import {
  BookPlus,
  RotateCcw,
  RefreshCw
} from 'lucide-react';

const CirculationDesk = () => {
  const [loans, setLoans] = useState([]);
  const [toast, setToast] = useState(null);

  // Modals
  const [isIssueModalOpen, setIsIssueModalOpen] = useState(false);
  const [isReturnModalOpen, setIsReturnModalOpen] = useState(false);

  // Issue Form
  const [issueData, setIssueData] = useState({
    userId: 3, // Default Member 1 ID
    bookId: 1,
    barcode: '',
    loanDays: 14
  });

  // Return Form
  const [returnData, setReturnData] = useState({
    transactionId: '',
    barcode: '',
    copyCondition: 'AVAILABLE'
  });

  const [submitting, setSubmitting] = useState(false);

  const fetchLoans = useCallback(async () => {
    try {
      const data = await circulationService.getAllLoans();
      setLoans(data || []);
    } catch (err) {
      console.error(err);
    }
  }, []);

  useEffect(() => {
    fetchLoans();
  }, [fetchLoans]);

  const handleIssueSubmit = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    try {
      await circulationService.issueBook({
        userId: parseInt(issueData.userId),
        bookId: issueData.bookId ? parseInt(issueData.bookId) : null,
        barcode: issueData.barcode,
        loanDays: parseInt(issueData.loanDays) || 14
      });
      setToast({ message: 'Book issued successfully!', type: 'success' });
      setIsIssueModalOpen(false);
      fetchLoans();
    } catch (err) {
      setToast({ message: err.response?.data?.message || 'Failed to issue book', type: 'error' });
    } finally {
      setSubmitting(false);
    }
  };

  const handleReturnSubmit = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    try {
      const res = await circulationService.returnBook({
        transactionId: returnData.transactionId ? parseInt(returnData.transactionId) : null,
        barcode: returnData.barcode,
        copyCondition: returnData.copyCondition
      });
      setToast({
        message: `Book returned successfully! ${res.fineAmount > 0 ? `Overdue Fine: ₹${res.fineAmount}` : ''}`,
        type: 'success'
      });
      setIsReturnModalOpen(false);
      fetchLoans();
    } catch (err) {
      setToast({ message: err.response?.data?.message || 'Failed to return book', type: 'error' });
    } finally {
      setSubmitting(false);
    }
  };

  const handleRenew = async (txId) => {
    try {
      await circulationService.renewBook(txId);
      setToast({ message: 'Loan renewed successfully for +14 days!', type: 'success' });
      fetchLoans();
    } catch (err) {
      setToast({ message: err.response?.data?.message || 'Renewal failed', type: 'error' });
    }
  };

  return (
    <div className="space-y-6 animate-fade-in">
      {toast && <Toast {...toast} onClose={() => setToast(null)} />}

      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-extrabold text-slate-100 tracking-tight">Circulation Desk</h1>
          <p className="text-xs text-slate-400 mt-1">Issue new items to members, process returns, and handle renewals.</p>
        </div>

        <div className="flex items-center gap-3">
          <button
            onClick={() => setIsReturnModalOpen(true)}
            className="px-4 py-2.5 bg-emerald-600 hover:bg-emerald-500 text-white rounded-xl text-xs font-semibold shadow-lg shadow-emerald-600/30 transition-all flex items-center gap-2"
          >
            <RotateCcw className="w-4 h-4" /> Process Return
          </button>
          <button
            onClick={() => setIsIssueModalOpen(true)}
            className="px-4 py-2.5 bg-indigo-600 hover:bg-indigo-500 text-white rounded-xl text-xs font-semibold shadow-lg shadow-indigo-600/30 transition-all flex items-center gap-2"
          >
            <BookPlus className="w-4 h-4" /> Issue Book
          </button>
        </div>
      </div>

      {/* Active Borrowings Table */}
      <div className="glass-panel rounded-3xl border border-slate-800 overflow-hidden shadow-2xl">
        <div className="p-5 border-b border-slate-800 bg-slate-900/40 flex items-center justify-between">
          <h3 className="text-sm font-bold text-slate-200">Active Borrowings & History</h3>
          <span className="text-xs text-slate-400">{loans.length} Total Loan Records</span>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full text-left text-xs text-slate-300">
            <thead className="bg-slate-900/80 text-slate-400 uppercase font-semibold border-b border-slate-800 text-[11px]">
              <tr>
                <th className="px-5 py-3.5">Tx ID</th>
                <th className="px-5 py-3.5">Member</th>
                <th className="px-5 py-3.5">Book & Barcode</th>
                <th className="px-5 py-3.5">Issue Date</th>
                <th className="px-5 py-3.5">Due Date</th>
                <th className="px-5 py-3.5">Status</th>
                <th className="px-5 py-3.5 text-right">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-800/60">
              {loans.length === 0 ? (
                <tr>
                  <td colSpan="7" className="text-center py-8 text-slate-500">
                    No active loan records found.
                  </td>
                </tr>
              ) : (
                loans.map((tx) => (
                  <tr key={tx.id} className="hover:bg-slate-800/40 transition-colors">
                    <td className="px-5 py-4 font-mono font-bold text-slate-400">#{tx.id}</td>
                    <td className="px-5 py-4">
                      <div className="font-semibold text-slate-200">{tx.userName}</div>
                      <div className="text-[11px] text-slate-400">{tx.userEmail}</div>
                    </td>
                    <td className="px-5 py-4">
                      <div className="font-semibold text-slate-200">{tx.bookTitle}</div>
                      <div className="font-mono text-[11px] text-indigo-400">{tx.barcode}</div>
                    </td>
                    <td className="px-5 py-4">{new Date(tx.issueDate).toLocaleDateString()}</td>
                    <td className="px-5 py-4 font-semibold text-slate-200">{new Date(tx.dueDate).toLocaleDateString()}</td>
                    <td className="px-5 py-4">
                      <span className={`px-2.5 py-1 text-[11px] font-bold rounded-full border ${
                        tx.status === 'RETURNED'
                          ? 'bg-slate-800 text-slate-400 border-slate-700'
                          : tx.status === 'OVERDUE'
                          ? 'bg-rose-500/20 text-rose-300 border-rose-500/30'
                          : 'bg-emerald-500/20 text-emerald-300 border-emerald-500/30'
                      }`}>
                        {tx.status}
                      </span>
                    </td>
                    <td className="px-5 py-4 text-right">
                      {tx.status !== 'RETURNED' && (
                        <div className="flex items-center justify-end gap-2">
                          <button
                            onClick={() => handleRenew(tx.id)}
                            className="px-2.5 py-1 bg-indigo-600/20 hover:bg-indigo-600/40 text-indigo-300 border border-indigo-500/30 rounded-lg text-[11px] font-semibold transition-colors flex items-center gap-1"
                          >
                            <RefreshCw className="w-3 h-3" /> Renew ({tx.renewalCount}/2)
                          </button>
                        </div>
                      )}
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* Issue Book Modal */}
      <Modal isOpen={isIssueModalOpen} onClose={() => setIsIssueModalOpen(false)} title="Issue Book to Member">
        <form onSubmit={handleIssueSubmit} className="space-y-4">
          <div>
            <label className="block text-xs font-semibold text-slate-300 mb-1">Member User ID *</label>
            <input
              type="number"
              required
              value={issueData.userId}
              onChange={(e) => setIssueData({ ...issueData, userId: e.target.value })}
              placeholder="e.g. 3"
              className="w-full px-3 py-2 bg-slate-900 border border-slate-800 rounded-xl text-xs text-slate-100"
            />
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-xs font-semibold text-slate-300 mb-1">Book ID (Auto Copy)</label>
              <input
                type="number"
                value={issueData.bookId || ''}
                onChange={(e) => setIssueData({ ...issueData, bookId: e.target.value })}
                placeholder="e.g. 1"
                className="w-full px-3 py-2 bg-slate-900 border border-slate-800 rounded-xl text-xs text-slate-100"
              />
            </div>
            <div>
              <label className="block text-xs font-semibold text-slate-300 mb-1">OR Copy Barcode</label>
              <input
                type="text"
                value={issueData.barcode}
                onChange={(e) => setIssueData({ ...issueData, barcode: e.target.value })}
                placeholder="e.g. BC-1-1"
                className="w-full px-3 py-2 bg-slate-900 border border-slate-800 rounded-xl text-xs text-slate-100"
              />
            </div>
          </div>

          <div>
            <label className="block text-xs font-semibold text-slate-300 mb-1">Loan Period (Days)</label>
            <input
              type="number"
              value={issueData.loanDays}
              onChange={(e) => setIssueData({ ...issueData, loanDays: e.target.value })}
              className="w-full px-3 py-2 bg-slate-900 border border-slate-800 rounded-xl text-xs text-slate-100"
            />
          </div>

          <button
            type="submit"
            disabled={submitting}
            className="w-full py-2.5 bg-indigo-600 hover:bg-indigo-500 text-white font-semibold text-xs rounded-xl shadow-lg shadow-indigo-600/30 transition-all"
          >
            {submitting ? 'Issuing...' : 'Confirm Book Checkout'}
          </button>
        </form>
      </Modal>

      {/* Return Book Modal */}
      <Modal isOpen={isReturnModalOpen} onClose={() => setIsReturnModalOpen(false)} title="Process Book Return">
        <form onSubmit={handleReturnSubmit} className="space-y-4">
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-xs font-semibold text-slate-300 mb-1">Transaction ID</label>
              <input
                type="number"
                value={returnData.transactionId}
                onChange={(e) => setReturnData({ ...returnData, transactionId: e.target.value })}
                placeholder="e.g. 500"
                className="w-full px-3 py-2 bg-slate-900 border border-slate-800 rounded-xl text-xs text-slate-100"
              />
            </div>
            <div>
              <label className="block text-xs font-semibold text-slate-300 mb-1">OR Copy Barcode</label>
              <input
                type="text"
                value={returnData.barcode}
                onChange={(e) => setReturnData({ ...returnData, barcode: e.target.value })}
                placeholder="e.g. BC-1-1"
                className="w-full px-3 py-2 bg-slate-900 border border-slate-800 rounded-xl text-xs text-slate-100"
              />
            </div>
          </div>

          <div>
            <label className="block text-xs font-semibold text-slate-300 mb-1">Item Physical Condition</label>
            <select
              value={returnData.copyCondition}
              onChange={(e) => setReturnData({ ...returnData, copyCondition: e.target.value })}
              className="w-full px-3 py-2 bg-slate-900 border border-slate-800 rounded-xl text-xs text-slate-200"
            >
              <option value="AVAILABLE">Available (Good condition)</option>
              <option value="DAMAGED">Damaged (Requires Repair)</option>
              <option value="LOST">Lost Item</option>
            </select>
          </div>

          <button
            type="submit"
            disabled={submitting}
            className="w-full py-2.5 bg-emerald-600 hover:bg-emerald-500 text-white font-semibold text-xs rounded-xl shadow-lg shadow-emerald-600/30 transition-all"
          >
            {submitting ? 'Processing Return...' : 'Complete Return & Calculate Fine'}
          </button>
        </form>
      </Modal>
    </div>
  );
};

export default CirculationDesk;
