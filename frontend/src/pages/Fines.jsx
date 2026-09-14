import React, { useState, useEffect, useCallback } from 'react';
import { fineService } from '../services/fineService';
import { useAuth } from '../context/AuthContext';
import Modal from '../components/Modal';
import Toast from '../components/Toast';
import { DollarSign, CreditCard, Gift } from 'lucide-react';

const Fines = () => {
  const { isLibrarian } = useAuth();
  const [fines, setFines] = useState([]);
  const [loading, setLoading] = useState(true);
  const [toast, setToast] = useState(null);

  // Payment Modal
  const [selectedFine, setSelectedFine] = useState(null);
  const [isPayModalOpen, setIsPayModalOpen] = useState(false);
  const [isWaiveModalOpen, setIsWaiveModalOpen] = useState(false);
  const [paymentMethod, setPaymentMethod] = useState('CREDIT_CARD');
  const [waiveReason, setWaiveReason] = useState('Librarian administrative courtesy');
  const [submitting, setSubmitting] = useState(false);

  const fetchFines = useCallback(async () => {
    setLoading(true);
    try {
      const data = isLibrarian ? await fineService.getAllFines() : await fineService.getMyFines();
      setFines(data || []);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  }, [isLibrarian]);

  useEffect(() => {
    fetchFines();
  }, [fetchFines]);

  const handlePaySubmit = async (e) => {
    e.preventDefault();
    if (!selectedFine) return;
    setSubmitting(true);
    try {
      await fineService.payFine(selectedFine.id, { paymentMethod });
      setToast({ message: `Fine of ₹${selectedFine.amount} paid successfully!`, type: 'success' });
      setIsPayModalOpen(false);
      fetchFines();
    } catch (err) {
      setToast({ message: err.response?.data?.message || 'Payment failed', type: 'error' });
    } finally {
      setSubmitting(false);
    }
  };

  const handleWaiveSubmit = async (e) => {
    e.preventDefault();
    if (!selectedFine) return;
    setSubmitting(true);
    try {
      await fineService.waiveFine(selectedFine.id, waiveReason);
      setToast({ message: `Fine of ₹${selectedFine.amount} waived successfully!`, type: 'success' });
      setIsWaiveModalOpen(false);
      fetchFines();
    } catch (err) {
      setToast({ message: err.response?.data?.message || 'Waive failed', type: 'error' });
    } finally {
      setSubmitting(false);
    }
  };

  const pendingTotal = fines
    .filter(f => f.status === 'PENDING')
    .reduce((acc, curr) => acc + curr.amount, 0);

  return (
    <div className="space-y-6 animate-fade-in">
      {toast && <Toast {...toast} onClose={() => setToast(null)} />}

      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-extrabold text-slate-100 tracking-tight">Fine & Overdue Management</h1>
          <p className="text-xs text-slate-400 mt-1">View accrued overdue fines, process payments, or record waivers.</p>
        </div>

        <div className="p-3 rounded-2xl glass-panel border border-rose-500/30 bg-rose-500/10 flex items-center gap-3">
          <DollarSign className="w-6 h-6 text-rose-400" />
          <div>
            <span className="text-[10px] text-slate-400 uppercase font-semibold block">Total Pending Fines</span>
            <span className="text-lg font-bold text-rose-300">₹{pendingTotal.toFixed(2)}</span>
          </div>
        </div>
      </div>

      <div className="glass-panel rounded-3xl border border-slate-800 overflow-hidden shadow-2xl">
        <div className="p-5 border-b border-slate-800 bg-slate-900/40 flex items-center justify-between">
          <h3 className="text-sm font-bold text-slate-200">Fine Records</h3>
          <span className="text-xs text-slate-400">{fines.length} Total Fine Items</span>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full text-left text-xs text-slate-300">
            <thead className="bg-slate-900/80 text-slate-400 uppercase font-semibold border-b border-slate-800 text-[11px]">
              <tr>
                <th className="px-5 py-3.5">Fine ID</th>
                <th className="px-5 py-3.5">Member</th>
                <th className="px-5 py-3.5">Book Title</th>
                <th className="px-5 py-3.5">Amount</th>
                <th className="px-5 py-3.5">Reason</th>
                <th className="px-5 py-3.5">Status</th>
                <th className="px-5 py-3.5 text-right">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-800/60">
              {loading ? (
                <tr>
                  <td colSpan="7" className="text-center py-8">
                    <div className="w-6 h-6 border-2 border-indigo-500 border-t-transparent rounded-full animate-spin mx-auto" />
                  </td>
                </tr>
              ) : fines.length === 0 ? (
                <tr>
                  <td colSpan="7" className="text-center py-8 text-slate-500">
                    No fine records found.
                  </td>
                </tr>
              ) : (
                fines.map((fine) => (
                  <tr key={fine.id} className="hover:bg-slate-800/40 transition-colors">
                    <td className="px-5 py-4 font-mono font-bold text-slate-400">#{fine.id}</td>
                    <td className="px-5 py-4 font-semibold text-slate-200">{fine.userName}</td>
                    <td className="px-5 py-4 text-slate-300 font-medium">{fine.bookTitle}</td>
                    <td className="px-5 py-4 font-bold text-slate-100">₹{fine.amount?.toFixed(2)}</td>
                    <td className="px-5 py-4 text-slate-400 text-[11px] max-w-xs truncate">{fine.reason}</td>
                    <td className="px-5 py-4">
                      <span className={`px-2.5 py-1 text-[11px] font-bold rounded-full border ${
                        fine.status === 'PAID'
                          ? 'bg-emerald-500/20 text-emerald-300 border-emerald-500/30'
                          : fine.status === 'WAIVED'
                          ? 'bg-sky-500/20 text-sky-300 border-sky-500/30'
                          : 'bg-rose-500/20 text-rose-300 border-rose-500/30'
                      }`}>
                        {fine.status}
                      </span>
                    </td>
                    <td className="px-5 py-4 text-right">
                      {fine.status === 'PENDING' && (
                        <div className="flex items-center justify-end gap-2">
                          <button
                            onClick={() => { setSelectedFine(fine); setIsPayModalOpen(true); }}
                            className="px-2.5 py-1 bg-emerald-600/20 hover:bg-emerald-600/40 text-emerald-300 border border-emerald-500/30 rounded-lg text-[11px] font-semibold transition-colors flex items-center gap-1"
                          >
                            <CreditCard className="w-3 h-3" /> Pay
                          </button>
                          {isLibrarian && (
                            <button
                              onClick={() => { setSelectedFine(fine); setIsWaiveModalOpen(true); }}
                              className="px-2.5 py-1 bg-sky-600/20 hover:bg-sky-600/40 text-sky-300 border border-sky-500/30 rounded-lg text-[11px] font-semibold transition-colors flex items-center gap-1"
                            >
                              <Gift className="w-3 h-3" /> Waive
                            </button>
                          )}
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

      {/* Pay Fine Modal */}
      <Modal isOpen={isPayModalOpen} onClose={() => setIsPayModalOpen(false)} title="Process Fine Payment">
        {selectedFine && (
          <form onSubmit={handlePaySubmit} className="space-y-4">
            <div className="p-4 rounded-xl bg-slate-900/80 border border-slate-800 text-xs text-slate-300 space-y-1">
              <div><span className="text-slate-500">Fine Amount:</span> <span className="font-bold text-rose-400 text-base">₹{selectedFine.amount?.toFixed(2)}</span></div>
              <div><span className="text-slate-500">Book:</span> <span className="font-semibold text-slate-200">{selectedFine.bookTitle}</span></div>
            </div>


            <div>
              <label className="block text-xs font-semibold text-slate-300 mb-1">Payment Method</label>
              <select
                value={paymentMethod}
                onChange={(e) => setPaymentMethod(e.target.value)}
                className="w-full px-3 py-2 bg-slate-900 border border-slate-800 rounded-xl text-xs text-slate-200"
              >
                <option value="CREDIT_CARD">Credit / Debit Card</option>
                <option value="CASH">Cash Payment at Desk</option>
                <option value="ONLINE_BANKING">Online Portal Transfer</option>
              </select>
            </div>

            <button
              type="submit"
              disabled={submitting}
              className="w-full py-2.5 bg-emerald-600 hover:bg-emerald-500 text-white font-semibold text-xs rounded-xl shadow-lg shadow-emerald-600/30 transition-all"
            >
              {submitting ? 'Processing Payment...' : 'Confirm Fine Payment'}
            </button>
          </form>
        )}
      </Modal>

      {/* Waive Fine Modal */}
      <Modal isOpen={isWaiveModalOpen} onClose={() => setIsWaiveModalOpen(false)} title="Waive Fine (Librarian)">
        {selectedFine && (
          <form onSubmit={handleWaiveSubmit} className="space-y-4">
            <div className="p-4 rounded-xl bg-slate-900/80 border border-slate-800 text-xs text-slate-300">
              <div><span className="text-slate-500">Waiving Amount:</span> <span className="font-bold text-sky-400">₹{selectedFine.amount?.toFixed(2)}</span></div>
              <div><span className="text-slate-500">Member:</span> <span className="font-semibold text-slate-200">{selectedFine.userName}</span></div>
            </div>

            <div>
              <label className="block text-xs font-semibold text-slate-300 mb-1">Reason for Waiver *</label>
              <input
                type="text"
                required
                value={waiveReason}
                onChange={(e) => setWaiveReason(e.target.value)}
                placeholder="e.g. Medical emergency extension / Staff decision"
                className="w-full px-3 py-2 bg-slate-900 border border-slate-800 rounded-xl text-xs text-slate-100"
              />
            </div>

            <button
              type="submit"
              disabled={submitting}
              className="w-full py-2.5 bg-sky-600 hover:bg-sky-500 text-white font-semibold text-xs rounded-xl shadow-lg shadow-sky-600/30 transition-all"
            >
              {submitting ? 'Waiving Fine...' : 'Confirm Fine Waiver'}
            </button>
          </form>
        )}
      </Modal>
    </div>
  );
};

export default Fines;
