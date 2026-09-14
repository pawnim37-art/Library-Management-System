import React from 'react';
import { NavLink } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import {
  LayoutDashboard,
  BookOpen,
  ArrowUpDown,
  BookMarked,
  DollarSign,
  BookmarkCheck,
  FileSpreadsheet,
  User
} from 'lucide-react';

const Sidebar = () => {
  const { user, isLibrarian } = useAuth();
  if (!user) return null;

  const navItems = [
    { label: 'Dashboard', path: '/', icon: LayoutDashboard, role: 'ALL' },
    { label: 'Book Catalog', path: '/catalog', icon: BookOpen, role: 'ALL' },
    { label: 'Circulation Desk', path: '/circulation-desk', icon: ArrowUpDown, role: 'STAFF' },
    { label: 'My Borrowings', path: '/my-loans', icon: BookMarked, role: 'MEMBER' },
    { label: 'Reservations & Holds', path: '/reservations', icon: BookmarkCheck, role: 'ALL' },
    { label: 'Fine Management', path: '/fines', icon: DollarSign, role: 'ALL' },
    { label: 'System Reports', path: '/reports', icon: FileSpreadsheet, role: 'STAFF' },
    { label: 'My Profile', path: '/profile', icon: User, role: 'ALL' },
  ];

  const filteredItems = navItems.filter(item => {
    if (item.role === 'ALL') return true;
    if (item.role === 'STAFF') return isLibrarian;
    if (item.role === 'MEMBER') return !isLibrarian;
    return true;
  });

  return (
    <aside className="w-64 glass-panel border-r border-slate-800 min-h-[calc(100vh-4rem)] p-4 flex flex-col justify-between hidden md:flex">
      <div className="space-y-1">
        <div className="px-3 py-2 text-[10px] font-bold text-slate-500 uppercase tracking-wider">
          Main Navigation
        </div>
        {filteredItems.map(item => (
          <NavLink
            key={item.path}
            to={item.path}
            end={item.path === '/'}
            className={({ isActive }) =>
              `flex items-center gap-3 px-3.5 py-2.5 rounded-xl text-xs font-semibold transition-all duration-200 ${
                isActive
                  ? 'bg-indigo-600/20 text-indigo-300 border border-indigo-500/30 shadow-md shadow-indigo-950/50'
                  : 'text-slate-400 hover:text-slate-200 hover:bg-slate-800/60'
              }`
            }
          >
            <item.icon className="w-4 h-4" />
            <span>{item.label}</span>
          </NavLink>
        ))}
      </div>

      {/* System Status Footer */}
      <div className="p-3.5 rounded-xl bg-slate-900/60 border border-slate-800/80">
        <div className="flex items-center justify-between text-[11px] font-medium text-slate-400">
          <span>Backend DB</span>
          <span className="flex items-center gap-1.5 text-emerald-400 font-semibold">
            <span className="w-2 h-2 rounded-full bg-emerald-500 animate-ping" /> Active
          </span>
        </div>
      </div>
    </aside>
  );
};

export default Sidebar;
