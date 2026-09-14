import React, { useState, useEffect } from 'react';
import { circulationService } from '../services/circulationService';
import Toast from '../components/Toast';
import { BookOpen, RefreshCw, Clock, AlertTriangle, CheckCircle } from 'lucide-react';

const MyLoans = () => {
  const [loans, setLoans] = useState([]);
  const [loading, setLoading] = useState(true);
  const [toast, setToast] = useState(null);

  useEffect(() => {
    fetchMyLoans();
  }, []);

  const fetchMyLoans = async () => {
    setLoading(true);
    try {
      const data = await circulationService.getMyLoans();
      setLoans(data || []);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleRenew = async (txId) => {
    try {
      await circulationService.renewBook(txId);
      setToast({ message: 'Loan renewed successfully for +14 days!', type: 'success' });
      fetchMyLoans();
    } catch (err) {
      setToast({ message: err.response?.data?.message || 'Renewal failed', type: 'error' });
    }
  };

  return (
    <div className="space-y-6 animate-fade-in">
      {toast && <Toast {...toast} onClose={() => setToast(null)} />}

      <div>
        <h1 className="text-2xl font-extrabold text-slate-100 tracking-tight">My Borrowed Books & History</h1>
        <p className="text-xs text-slate-400 mt-1">Review active loans, due dates, renewal limits, and past returned items.</p>
      </div>

      <div className="glass-panel rounded-3xl border border-slate-800 overflow-hidden shadow-2xl">
        <div className="p-5 border-b border-slate-800 bg-slate-900/40 flex items-center justify-between">
          <h3 className="text-sm font-bold text-slate-200">Borrowing Log</h3>
          <span className="text-xs text-slate-400">{loans.length} Total Loans</span>
        </div>

        {loading ? (
          <div className="p-12 text-center">
            <div className="w-6 h-6 border-2 border-indigo-500 border-t-transparent rounded-full animate-spin mx-auto" />
          </div>
        ) : loans.length === 0 ? (
          <div className="p-12 text-center text-slate-500 text-xs">
            You haven't borrowed any books yet. Browse the catalog to borrow your first title!
          </div>
        ) : (
          <div className="divide-y divide-slate-800/60">
            {loans.map((tx) => (
              <div key={tx.id} className="p-5 flex flex-col sm:flex-row sm:items-center justify-between gap-4 hover:bg-slate-800/30 transition-colors">
                <div className="flex items-center gap-4">
                  <img
                    src={tx.coverImageUrl || 'https://images.unsplash.com/photo-1543002588-bfa74002ed7e?w=500&q=80'}
                    alt={tx.bookTitle}
                    className="w-12 h-16 object-cover rounded-xl border border-slate-800 shadow-md"
                  />
                  <div>
                    <h4 className="text-sm font-bold text-slate-100">{tx.bookTitle}</h4>
                    <p className="text-xs text-slate-400 font-mono mt-0.5">Barcode: {tx.barcode} | ISBN: {tx.bookIsbn}</p>
                    <div className="flex items-center gap-3 text-xs text-slate-400 mt-1.5">
                      <span>Issued: {new Date(tx.issueDate).toLocaleDateString()}</span>
                      <span className="font-semibold text-slate-200">Due: {new Date(tx.dueDate).toLocaleDateString()}</span>
                    </div>
                  </div>
                </div>

                <div className="flex items-center gap-3">
                  <span className={`px-3 py-1 text-xs font-bold rounded-full border ${
                    tx.status === 'RETURNED'
                      ? 'bg-slate-800 text-slate-400 border-slate-700'
                      : tx.status === 'OVERDUE'
                      ? 'bg-rose-500/20 text-rose-300 border-rose-500/30'
                      : 'bg-emerald-500/20 text-emerald-300 border-emerald-500/30'
                  }`}>
                    {tx.status}
                  </span>

                  {tx.status !== 'RETURNED' && (
                    <button
                      onClick={() => handleRenew(tx.id)}
                      className="px-3 py-1.5 bg-indigo-600/20 hover:bg-indigo-600/40 text-indigo-300 border border-indigo-500/30 rounded-xl text-xs font-semibold transition-colors flex items-center gap-1.5"
                    >
                      <RefreshCw className="w-3.5 h-3.5" /> Renew ({tx.renewalCount}/2)
                    </button>
                  )}
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default MyLoans;
