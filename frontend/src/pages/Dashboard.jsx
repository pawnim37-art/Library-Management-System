import React, { useState, useEffect, useCallback } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { dashboardService } from '../services/dashboardService';
import { circulationService } from '../services/circulationService';
import { fineService } from '../services/fineService';
import StatCard from '../components/StatCard';
import {
  BookOpen,
  Users,
  ArrowUpDown,
  AlertTriangle,
  DollarSign,
  FileText,
  TrendingUp,
  Clock,
  Sparkles
} from 'lucide-react';

const Dashboard = () => {
  const { user, isLibrarian } = useAuth();

  const [stats, setStats] = useState(null);
  const [memberLoans, setMemberLoans] = useState([]);
  const [memberFines, setMemberFines] = useState([]);
  const [loading, setLoading] = useState(true);

  const fetchDashboardData = useCallback(async () => {
    setLoading(true);
    try {
      if (isLibrarian) {
        const data = await dashboardService.getStats();
        setStats(data);
      } else {
        const loans = await circulationService.getMyLoans();
        const fines = await fineService.getMyFines();
        setMemberLoans(loans || []);
        setMemberFines(fines || []);
      }
    } catch (err) {
      console.error('Failed to load dashboard data', err);
    } finally {
      setLoading(false);
    }
  }, [isLibrarian]);

  useEffect(() => {
    fetchDashboardData();
  }, [fetchDashboardData]);

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-[60vh]">
        <div className="flex flex-col items-center gap-3">
          <div className="w-10 h-10 border-4 border-indigo-500/30 border-t-indigo-500 rounded-full animate-spin" />
          <span className="text-sm font-medium text-slate-400">Loading Dashboard Metrics...</span>
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-8 animate-fade-in">
      {/* Welcome Banner */}
      <div className="relative overflow-hidden rounded-3xl glass-panel p-8 border border-slate-800 bg-gradient-to-r from-indigo-950/80 via-slate-900 to-violet-950/70 shadow-2xl">
        <div className="relative z-10">
          <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-indigo-500/10 border border-indigo-500/20 text-indigo-300 text-xs font-semibold mb-3">
            <Sparkles className="w-3.5 h-3.5" /> LibFlow Workspace Dashboard
          </div>
          <h1 className="text-3xl font-extrabold text-slate-100 tracking-tight">
            Hello, {user?.fullName}! 👋
          </h1>
          <p className="text-sm text-slate-400 mt-1 max-w-xl">
            {isLibrarian
              ? 'Real-time overview of library catalog, circulation desk stats, active borrowings, and overdue fine analytics.'
              : 'Track your current book loans, upcoming due dates, hold reservations, and payment history.'}
          </p>

          {/* Quick Action Buttons */}
          <div className="flex flex-wrap gap-3 mt-6">
            <Link
              to="/catalog"
              className="px-4 py-2.5 rounded-xl bg-indigo-600 hover:bg-indigo-500 text-white font-semibold text-xs shadow-lg shadow-indigo-600/30 transition-all flex items-center gap-2 hover:scale-[1.02]"
            >
              <BookOpen className="w-4 h-4" /> Browse Catalog
            </Link>
            {isLibrarian && (
              <>
                <Link
                  to="/circulation-desk"
                  className="px-4 py-2.5 rounded-xl bg-slate-800 hover:bg-slate-700 text-slate-200 border border-slate-700 font-semibold text-xs transition-all flex items-center gap-2"
                >
                  <ArrowUpDown className="w-4 h-4 text-sky-400" /> Issue / Return Desk
                </Link>
                <Link
                  to="/reports"
                  className="px-4 py-2.5 rounded-xl bg-slate-800 hover:bg-slate-700 text-slate-200 border border-slate-700 font-semibold text-xs transition-all flex items-center gap-2"
                >
                  <FileText className="w-4 h-4 text-purple-400" /> Export System Reports
                </Link>
              </>
            )}
          </div>
        </div>
      </div>

      {/* Staff Dashboard (Admin / Librarian) */}
      {isLibrarian && stats && (
        <>
          {/* Key Metrics Grid */}
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-5">
            <StatCard
              title="Total Titles"
              value={stats.totalBooks}
              icon={BookOpen}
              color="indigo"
              subtitle={`${stats.totalCopies} physical copies total`}
            />
            <StatCard
              title="Active Members"
              value={stats.activeMembers}
              icon={Users}
              color="sky"
              subtitle="Registered patrons"
            />
            <StatCard
              title="Checked Out Books"
              value={stats.issuedBooksCount}
              icon={ArrowUpDown}
              color="emerald"
              subtitle={`${stats.pendingReservationsCount} active hold reservations`}
            />
            <StatCard
              title="Overdue Items"
              value={stats.overdueBooksCount}
              icon={AlertTriangle}
              color="rose"
              subtitle={`Pending Fines: ₹${stats.pendingFinesAmount || '0.00'}`}
            />
          </div>

          {/* Visual Widgets Section */}
          <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
            {/* Top Borrowed Books Widget */}
            <div className="lg:col-span-2 glass-panel p-6 rounded-3xl border border-slate-800 flex flex-col justify-between">
              <div>
                <div className="flex items-center justify-between mb-5">
                  <div className="flex items-center gap-2">
                    <TrendingUp className="w-5 h-5 text-indigo-400" />
                    <h3 className="text-base font-bold text-slate-100">Top Borrowed Books</h3>
                  </div>
                  <span className="text-xs text-slate-400">Popularity Analytics</span>
                </div>

                <div className="space-y-3.5">
                  {stats.topBorrowedBooks?.length === 0 ? (
                    <p className="text-xs text-slate-500">No circulation activity recorded yet.</p>
                  ) : (
                    stats.topBorrowedBooks?.map((book, idx) => (
                      <div
                        key={book.bookId}
                        className="flex items-center justify-between p-3.5 rounded-2xl bg-slate-900/60 border border-slate-800/80 hover:border-indigo-500/40 transition-colors"
                      >
                        <div className="flex items-center gap-3">
                          <span className="w-6 h-6 rounded-lg bg-indigo-500/20 text-indigo-300 font-bold text-xs flex items-center justify-center">
                            #{idx + 1}
                          </span>
                          <img
                            src={book.coverImageUrl || 'https://images.unsplash.com/photo-1543002588-bfa74002ed7e?w=500&q=80'}
                            alt={book.title}
                            className="w-9 h-12 object-cover rounded-md border border-slate-800"
                            onError={(e) => { e.target.src = 'https://images.unsplash.com/photo-1543002588-bfa74002ed7e?w=500&q=80'; }}
                          />
                          <div>
                            <h4 className="text-xs font-bold text-slate-200 line-clamp-1">{book.title}</h4>
                            <p className="text-[11px] text-slate-400">{book.authorName}</p>
                          </div>
                        </div>
                        <span className="px-3 py-1 text-xs font-bold text-indigo-300 bg-indigo-500/10 border border-indigo-500/20 rounded-full">
                          {book.borrowCount} Loans
                        </span>
                      </div>
                    ))
                  )}
                </div>
              </div>
            </div>

            {/* Department / Category Breakdown */}
            <div className="glass-panel p-6 rounded-3xl border border-slate-800">
              <h3 className="text-base font-bold text-slate-100 mb-4">Catalog Genres</h3>
              <div className="space-y-3">
                {stats.categoryDistribution?.map((cat) => (
                  <div key={cat.categoryName} className="p-3 rounded-2xl bg-slate-900/60 border border-slate-800">
                    <div className="flex items-center justify-between text-xs mb-1.5">
                      <span className="font-semibold text-slate-300">{cat.categoryName}</span>
                      <span className="font-bold text-indigo-400">{cat.bookCount} Titles</span>
                    </div>
                    <div className="w-full h-2 bg-slate-800 rounded-full overflow-hidden">
                      <div
                        className="h-full bg-gradient-to-r from-indigo-500 to-purple-500 rounded-full"
                        style={{ width: `${Math.min(100, (cat.bookCount / Math.max(1, stats.totalBooks)) * 100)}%` }}
                      />
                    </div>
                  </div>
                ))}
              </div>
            </div>
          </div>
        </>
      )}

      {/* Member Dashboard */}
      {!isLibrarian && (
        <div className="space-y-6">
          <div className="grid grid-cols-1 sm:grid-cols-3 gap-5">
            <StatCard
              title="My Active Loans"
              value={memberLoans.filter(l => l.status === 'ISSUED' || l.status === 'OVERDUE').length}
              icon={BookOpen}
              color="indigo"
              subtitle="Current checked out books"
            />
            <StatCard
              title="Borrowing History"
              value={memberLoans.length}
              icon={Clock}
              color="emerald"
              subtitle="Total books borrowed"
            />
            <StatCard
              title="Pending Fines"
              value={`₹${memberFines.filter(f => f.status === 'PENDING').reduce((acc, curr) => acc + curr.amount, 0).toFixed(2)}`}
              icon={DollarSign}
              color="rose"
              subtitle="Unpaid overdue charges"
            />
          </div>

          {/* Member Active Loans List */}
          <div className="glass-panel p-6 rounded-3xl border border-slate-800">
            <h3 className="text-base font-bold text-slate-100 mb-4">Currently Borrowed Books</h3>
            <div className="space-y-3">
              {memberLoans.filter(l => l.status === 'ISSUED' || l.status === 'OVERDUE').length === 0 ? (
                <p className="text-xs text-slate-500 text-center py-6">You have no active book loans right now.</p>
              ) : (
                memberLoans.filter(l => l.status === 'ISSUED' || l.status === 'OVERDUE').map(loan => (
                  <div key={loan.id} className="p-4 rounded-2xl bg-slate-900/60 border border-slate-800 flex items-center justify-between">
                    <div className="flex items-center gap-3">
                      <img
                        src={loan.coverImageUrl || 'https://images.unsplash.com/photo-1543002588-bfa74002ed7e?w=500&q=80'}
                        alt={loan.bookTitle}
                        className="w-10 h-14 object-cover rounded-lg border border-slate-800"
                      />
                      <div>
                        <h4 className="text-xs font-bold text-slate-200">{loan.bookTitle}</h4>
                        <p className="text-[11px] text-slate-400 font-mono">Barcode: {loan.barcode}</p>
                        <p className="text-[11px] text-slate-400">Due Date: {new Date(loan.dueDate).toLocaleDateString()}</p>
                      </div>
                    </div>
                    <span className={`px-2.5 py-1 text-xs font-semibold rounded-full border ${
                      loan.status === 'OVERDUE'
                        ? 'bg-rose-500/20 text-rose-300 border-rose-500/30'
                        : 'bg-emerald-500/20 text-emerald-300 border-emerald-500/30'
                    }`}>
                      {loan.status}
                    </span>
                  </div>
                ))
              )}
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default Dashboard;
