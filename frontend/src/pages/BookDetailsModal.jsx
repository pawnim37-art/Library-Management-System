import React, { useState } from 'react';
import Modal from '../components/Modal';
import { circulationService } from '../services/circulationService';
import Toast from '../components/Toast';
import {
  User,
  BookmarkPlus,
  Copy
} from 'lucide-react';

const BookDetailsModal = ({ book, isOpen, onClose, onRefresh }) => {
  const [toast, setToast] = useState(null);
  const [loading, setLoading] = useState(false);

  if (!book) return null;

  const handleBorrow = async () => {
    setLoading(true);
    try {
      await circulationService.borrowBook(book.id);
      setToast({ message: `Successfully borrowed '${book.title}'! Due in 14 days.`, type: 'success' });
      if (onRefresh) onRefresh();
    } catch (err) {
      setToast({ message: err.response?.data?.message || 'Failed to borrow book. Please sign in.', type: 'error' });
    } finally {
      setLoading(false);
    }
  };

  const handleReserve = async () => {
    setLoading(true);
    try {
      await circulationService.reserveBook(book.id);
      setToast({ message: `Successfully placed a hold reservation for '${book.title}'!`, type: 'success' });
      if (onRefresh) onRefresh();
    } catch (err) {
      setToast({ message: err.response?.data?.message || 'Failed to place reservation', type: 'error' });
    } finally {
      setLoading(false);
    }
  };

  const isAvailable = book.availableCopies > 0;

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Book Details & Availability" maxWidth="max-w-3xl">
      {toast && <Toast {...toast} onClose={() => setToast(null)} />}

      <div className="grid grid-cols-1 sm:grid-cols-3 gap-6">
        {/* Book Cover & Actions Column */}
        <div className="space-y-4">
          <div className="relative rounded-2xl overflow-hidden border border-slate-700 bg-slate-900 shadow-xl">
            <img
              src={book.coverImageUrl || 'https://images.unsplash.com/photo-1543002588-bfa74002ed7e?w=500&q=80'}
              alt={book.title}
              className="w-full h-72 object-cover"
              onError={(e) => { e.target.src = 'https://images.unsplash.com/photo-1543002588-bfa74002ed7e?w=500&q=80'; }}
            />
          </div>

          <div className="space-y-2">
            {isAvailable ? (
              <button
                onClick={handleBorrow}
                disabled={loading}
                className="w-full py-2.5 bg-emerald-600 hover:bg-emerald-500 text-white font-semibold text-xs rounded-xl shadow-lg shadow-emerald-600/30 transition-all flex items-center justify-center gap-2"
              >
                <BookmarkPlus className="w-4 h-4" /> Borrow This Book
              </button>
            ) : (
              <button
                onClick={handleReserve}
                disabled={loading}
                className="w-full py-2.5 bg-amber-600 hover:bg-amber-500 text-white font-semibold text-xs rounded-xl shadow-lg shadow-amber-600/30 transition-all flex items-center justify-center gap-2"
              >
                <BookmarkPlus className="w-4 h-4" /> Place Hold Reservation
              </button>
            )}
          </div>
        </div>

        {/* Detailed Info Column */}
        <div className="sm:col-span-2 space-y-4">
          <div>
            <span className="px-2.5 py-1 text-xs font-semibold bg-indigo-500/20 text-indigo-300 rounded-md border border-indigo-500/30">
              {book.categoryName} ({book.department || 'General'})
            </span>
            <h3 className="text-xl font-bold text-slate-100 mt-2">{book.title}</h3>
            <p className="text-xs text-slate-400 mt-1 flex items-center gap-1.5 font-medium">
              <User className="w-4 h-4 text-indigo-400" /> {book.authorName}
            </p>
          </div>

          <div className="grid grid-cols-2 gap-3 p-3 rounded-2xl bg-slate-900/60 border border-slate-800 text-xs">
            <div>
              <span className="text-slate-500 block">ISBN Number</span>
              <span className="font-mono text-slate-200 font-semibold">{book.isbn}</span>
            </div>
            <div>
              <span className="text-slate-500 block">Publisher & Year</span>
              <span className="text-slate-200 font-semibold">{book.publisher || 'N/A'} ({book.publishYear || 'N/A'})</span>
            </div>
            <div>
              <span className="text-slate-500 block">Total Copies</span>
              <span className="text-slate-200 font-semibold">{book.totalCopies} Copies</span>
            </div>
            <div>
              <span className="text-slate-500 block">Available Now</span>
              <span className={`font-semibold ${isAvailable ? 'text-emerald-400' : 'text-rose-400'}`}>
                {book.availableCopies} Available
              </span>
            </div>
          </div>

          <div>
            <h4 className="text-xs font-bold text-slate-300 uppercase tracking-wider mb-1">Book Description</h4>
            <p className="text-xs text-slate-400 leading-relaxed bg-slate-900/40 p-3 rounded-xl border border-slate-800">
              {book.description || 'No description available.'}
            </p>
          </div>

          {/* Physical Inventory Barcodes List */}
          <div>
            <h4 className="text-xs font-bold text-slate-300 uppercase tracking-wider mb-2 flex items-center gap-1.5">
              <Copy className="w-3.5 h-3.5 text-indigo-400" /> Physical Copy Barcodes
            </h4>
            <div className="max-h-36 overflow-y-auto custom-scrollbar space-y-1.5">
              {book.copies?.map((copy) => (
                <div key={copy.id} className="flex items-center justify-between px-3 py-2 rounded-xl bg-slate-900/60 border border-slate-800 text-xs">
                  <span className="font-mono font-semibold text-slate-300">{copy.barcode}</span>
                  <span className={`px-2 py-0.5 text-[10px] font-bold rounded-md border ${
                    copy.status === 'AVAILABLE'
                      ? 'bg-emerald-500/20 text-emerald-300 border-emerald-500/30'
                      : copy.status === 'BORROWED'
                      ? 'bg-amber-500/20 text-amber-300 border-amber-500/30'
                      : 'bg-rose-500/20 text-rose-300 border-rose-500/30'
                  }`}>
                    {copy.status}
                  </span>
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>
    </Modal>
  );
};

export default BookDetailsModal;
