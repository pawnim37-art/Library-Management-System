import React, { useState, useEffect, useCallback } from 'react';
import { authService } from '../services/authService';
import { useAuth } from '../context/AuthContext';
import Toast from '../components/Toast';
import { Save } from 'lucide-react';

const Profile = () => {
  const { setUser } = useAuth();
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [toast, setToast] = useState(null);
  const [submitting, setSubmitting] = useState(false);

  const [fullName, setFullName] = useState('');
  const [phone, setPhone] = useState('');
  const [address, setAddress] = useState('');

  const fetchProfile = useCallback(async () => {
    setLoading(true);
    try {
      const data = await authService.getProfile();
      setProfile(data);
      setFullName(data.fullName || '');
      setPhone(data.phone || '');
      setAddress(data.address || '');
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchProfile();
  }, [fetchProfile]);

  const handleUpdate = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    try {
      const updated = await authService.updateProfile({ fullName, phone, address });
      setProfile(updated);
      setUser(prev => ({ ...prev, fullName: updated.fullName }));
      setToast({ message: 'Profile details updated successfully!', type: 'success' });
    } catch (err) {
      setToast({ message: err.response?.data?.message || 'Failed to update profile', type: 'error' });
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-[50vh]">
        <div className="w-8 h-8 border-4 border-indigo-500/30 border-t-indigo-500 rounded-full animate-spin" />
      </div>
    );
  }

  return (
    <div className="max-w-3xl mx-auto space-y-6 animate-fade-in">
      {toast && <Toast {...toast} onClose={() => setToast(null)} />}

      <div>
        <h1 className="text-2xl font-extrabold text-slate-100 tracking-tight">Member Profile & Settings</h1>
        <p className="text-xs text-slate-400 mt-1">Manage your account information and contact preferences.</p>
      </div>

      <div className="glass-panel p-8 rounded-3xl border border-slate-800 space-y-6 shadow-2xl">
        <div className="flex items-center gap-4 pb-6 border-b border-slate-800">
          <div className="w-16 h-16 rounded-2xl bg-indigo-600/30 border border-indigo-500/40 flex items-center justify-center text-indigo-300 font-extrabold text-2xl shadow-lg">
            {profile?.fullName ? profile.fullName.charAt(0) : 'U'}
          </div>
          <div>
            <h2 className="text-lg font-bold text-slate-100">{profile?.fullName}</h2>
            <p className="text-xs text-slate-400 font-mono">{profile?.email}</p>
            <span className="inline-block mt-1 px-2.5 py-0.5 text-[10px] font-bold rounded-md bg-indigo-500/20 text-indigo-300 border border-indigo-500/30">
              {profile?.role}
            </span>
          </div>
        </div>

        {/* Membership Summary Grid */}
        <div className="grid grid-cols-3 gap-4 p-4 rounded-2xl bg-slate-900/60 border border-slate-800 text-center text-xs">
          <div>
            <span className="text-slate-500 block">Active Loans</span>
            <span className="text-base font-bold text-indigo-400">{profile?.activeLoansCount || 0}</span>
          </div>
          <div>
            <span className="text-slate-500 block">Total Borrowed</span>
            <span className="text-base font-bold text-slate-200">{profile?.totalBorrowCount || 0}</span>
          </div>
          <div>
            <span className="text-slate-500 block">Pending Fines</span>
            <span className="text-base font-bold text-rose-400">₹{profile?.pendingFinesAmount?.toFixed(2) || '0.00'}</span>
          </div>
        </div>

        {/* Profile Edit Form */}
        <form onSubmit={handleUpdate} className="space-y-4 pt-2">
          <div>
            <label className="block text-xs font-semibold text-slate-300 mb-1">Full Name</label>
            <input
              type="text"
              required
              value={fullName}
              onChange={(e) => setFullName(e.target.value)}
              className="w-full px-3.5 py-2.5 bg-slate-900/80 border border-slate-800 rounded-xl text-xs text-slate-100 focus:border-indigo-500"
            />
          </div>

          <div>
            <label className="block text-xs font-semibold text-slate-300 mb-1">Phone Number</label>
            <input
              type="text"
              value={phone}
              onChange={(e) => setPhone(e.target.value)}
              placeholder="+1 555-0199"
              className="w-full px-3.5 py-2.5 bg-slate-900/80 border border-slate-800 rounded-xl text-xs text-slate-100 focus:border-indigo-500"
            />
          </div>

          <div>
            <label className="block text-xs font-semibold text-slate-300 mb-1">Postal Address</label>
            <input
              type="text"
              value={address}
              onChange={(e) => setAddress(e.target.value)}
              placeholder="Full mailing address"
              className="w-full px-3.5 py-2.5 bg-slate-900/80 border border-slate-800 rounded-xl text-xs text-slate-100 focus:border-indigo-500"
            />
          </div>

          <button
            type="submit"
            disabled={submitting}
            className="py-2.5 px-6 bg-indigo-600 hover:bg-indigo-500 text-white font-semibold text-xs rounded-xl shadow-lg shadow-indigo-600/30 transition-all flex items-center gap-2"
          >
            <Save className="w-4 h-4" />
            {submitting ? 'Saving Changes...' : 'Save Profile Changes'}
          </button>
        </form>
      </div>
    </div>
  );
};

export default Profile;
