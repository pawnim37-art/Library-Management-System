import React from 'react';
import { User, ArrowRight } from 'lucide-react';

const BookCard = ({ book, onClick }) => {
  const isAvailable = book.availableCopies > 0;

  return (
    <div
      onClick={onClick}
      className="group glass-panel rounded-2xl border border-slate-800 hover:border-indigo-500/50 transition-all duration-300 hover:-translate-y-1 hover:shadow-2xl overflow-hidden cursor-pointer flex flex-col"
    >
      {/* Cover Image & Availability Badge */}
      <div className="relative h-60 w-full overflow-hidden bg-slate-900">
        <img
          src={book.coverImageUrl || 'https://images.unsplash.com/photo-1543002588-bfa74002ed7e?w=500&q=80'}
          alt={book.title}
          className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500"
          onError={(e) => {
            e.target.src = 'https://images.unsplash.com/photo-1543002588-bfa74002ed7e?w=500&q=80';
          }}
        />
        <div className="absolute inset-0 bg-gradient-to-t from-slate-950 via-transparent to-transparent opacity-80" />

        <div className="absolute top-3 right-3">
          <span className={`px-2.5 py-1 text-xs font-semibold rounded-full border backdrop-blur-md ${
            isAvailable
              ? 'bg-emerald-500/20 text-emerald-300 border-emerald-500/30'
              : 'bg-rose-500/20 text-rose-300 border-rose-500/30'
          }`}>
            {isAvailable ? `${book.availableCopies} Available` : 'Checked Out'}
          </span>
        </div>

        <div className="absolute bottom-3 left-3 right-3 flex items-center justify-between">
          <span className="px-2 py-0.5 text-xs font-medium bg-slate-900/80 text-indigo-300 rounded-md border border-slate-700/50 backdrop-blur-md">
            {book.categoryName}
          </span>
          <span className="text-xs text-slate-400 font-mono">ISBN: {book.isbn}</span>
        </div>
      </div>

      {/* Book Information */}
      <div className="p-5 flex-1 flex flex-col justify-between">
        <div>
          <h4 className="text-base font-bold text-slate-100 line-clamp-1 group-hover:text-indigo-400 transition-colors">
            {book.title}
          </h4>
          <p className="text-xs text-slate-400 mt-1 flex items-center gap-1.5 line-clamp-1">
            <User className="w-3.5 h-3.5 text-slate-500" /> {book.authorName}
          </p>
          <p className="text-xs text-slate-400 mt-2 line-clamp-2 leading-relaxed">
            {book.description || 'No description provided.'}
          </p>
        </div>

        <div className="mt-4 pt-3 border-t border-slate-800/80 flex items-center justify-between">
          <span className="text-xs text-slate-500 font-medium">Pub: {book.publishYear || 'N/A'}</span>
          <span className="text-xs font-semibold text-indigo-400 group-hover:translate-x-1 transition-transform flex items-center gap-1">
            View Details <ArrowRight className="w-3.5 h-3.5" />
          </span>
        </div>
      </div>
    </div>
  );
};

export default BookCard;
