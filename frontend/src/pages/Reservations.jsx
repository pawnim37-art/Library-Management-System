import React, { useState, useEffect } from 'react';
import { circulationService } from '../services/circulationService';
import { useAuth } from '../context/AuthContext';
import Toast from '../components/Toast';
import { BookmarkCheck, Clock, XCircle, AlertCircle } from 'lucide-react';

const Reservations = () => {
  const { isLibrarian } = useAuth();
  const [reservations, setReservations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [toast, setToast] = useState(null);

  useEffect(() => {
    fetchReservations();
  }, [isLibrarian]);

  const fetchReservations = async () => {
    setLoading(true);
    try {
      const data = isLibrarian ? await circulationService.getAllReservations() : await circulationService.getMyReservations();
      setReservations(data || []);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleCancel = async (resId) => {
    try {
      await circulationService.cancelReservation(resId);
      setToast({ message: 'Reservation cancelled successfully', type: 'success' });
      fetchReservations();
    } catch (err) {
      setToast({ message: err.response?.data?.message || 'Failed to cancel reservation', type: 'error' });
    }
  };

  return (
    <div className="space-y-6 animate-fade-in">
      {toast && <Toast {...toast} onClose={() => setToast(null)} />}

      <div>
        <h1 className="text-2xl font-extrabold text-slate-100 tracking-tight">Hold Queue & Reservations</h1>
        <p className="text-xs text-slate-400 mt-1">Manage active holds for books currently checked out or unavailable.</p>
      </div>

      <div className="glass-panel rounded-3xl border border-slate-800 overflow-hidden shadow-2xl">
        <div className="p-5 border-b border-slate-800 bg-slate-900/40 flex items-center justify-between">
          <h3 className="text-sm font-bold text-slate-200">Active Hold Queue</h3>
          <span className="text-xs text-slate-400">{reservations.length} Hold Requests</span>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full text-left text-xs text-slate-300">
            <thead className="bg-slate-900/80 text-slate-400 uppercase font-semibold border-b border-slate-800 text-[11px]">
              <tr>
                <th className="px-5 py-3.5">Res ID</th>
                <th className="px-5 py-3.5">Book Title</th>
                <th className="px-5 py-3.5">Member</th>
                <th className="px-5 py-3.5">Queue Position</th>
                <th className="px-5 py-3.5">Reserved Date</th>
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
              ) : reservations.length === 0 ? (
                <tr>
                  <td colSpan="7" className="text-center py-8 text-slate-500">
                    No active reservation holds.
                  </td>
                </tr>
              ) : (
                reservations.map((res) => (
                  <tr key={res.id} className="hover:bg-slate-800/40 transition-colors">
                    <td className="px-5 py-4 font-mono font-bold text-slate-400">#{res.id}</td>
                    <td className="px-5 py-4 font-semibold text-slate-200">{res.bookTitle}</td>
                    <td className="px-5 py-4">{res.userName}</td>
                    <td className="px-5 py-4 font-bold text-indigo-400">Position #{res.queuePosition || 1}</td>
                    <td className="px-5 py-4">{new Date(res.reservedAt).toLocaleDateString()}</td>
                    <td className="px-5 py-4">
                      <span className={`px-2.5 py-1 text-[11px] font-bold rounded-full border ${
                        res.status === 'FULFILLED'
                          ? 'bg-emerald-500/20 text-emerald-300 border-emerald-500/30'
                          : res.status === 'CANCELLED'
                          ? 'bg-slate-800 text-slate-400 border-slate-700'
                          : 'bg-amber-500/20 text-amber-300 border-amber-500/30'
                      }`}>
                        {res.status}
                      </span>
                    </td>
                    <td className="px-5 py-4 text-right">
                      {res.status === 'PENDING' && (
                        <button
                          onClick={() => handleCancel(res.id)}
                          className="px-2.5 py-1 bg-rose-600/20 hover:bg-rose-600/40 text-rose-300 border border-rose-500/30 rounded-lg text-[11px] font-semibold transition-colors flex items-center gap-1 ml-auto"
                        >
                          <XCircle className="w-3 h-3" /> Cancel
                        </button>
                      )}
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default Reservations;
