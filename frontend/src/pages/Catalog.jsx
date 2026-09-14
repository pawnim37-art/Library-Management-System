import React, { useState, useEffect, useCallback } from 'react';
import { bookService } from '../services/bookService';
import { useAuth } from '../context/AuthContext';
import BookCard from '../components/BookCard';
import BookDetailsModal from './BookDetailsModal';
import Modal from '../components/Modal';
import Toast from '../components/Toast';
import {
  Search,
  PlusCircle,
  Upload,
  BookOpen
} from 'lucide-react';

const Catalog = () => {
  const { isLibrarian } = useAuth();

  const [books, setBooks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedCategory, setSelectedCategory] = useState('');
  const [onlyAvailable, setOnlyAvailable] = useState(false);

  // Modals & Selected Book
  const [selectedBook, setSelectedBook] = useState(null);
  const [isDetailsOpen, setIsDetailsOpen] = useState(false);
  const [isAddModalOpen, setIsAddModalOpen] = useState(false);
  const [isCsvModalOpen, setIsCsvModalOpen] = useState(false);

  // Form State for Add/Edit Book
  const [formData, setFormData] = useState({
    title: '',
    isbn: '',
    authorName: '',
    authorBio: '',
    categoryName: 'Computer Science',
    department: 'Technology',
    publisher: '',
    publishYear: 2024,
    description: '',
    coverImageUrl: '',
    totalCopies: 2
  });

  const [csvFile, setCsvFile] = useState(null);
  const [toast, setToast] = useState(null);
  const [submitting, setSubmitting] = useState(false);

  const fetchBooks = useCallback(async () => {
    setLoading(true);
    try {
      let data = [];
      if (searchQuery.trim()) {
        data = await bookService.searchBooks(searchQuery);
      } else if (selectedCategory) {
        data = await bookService.getBooksByCategory(selectedCategory);
      } else {
        data = await bookService.getAllBooks();
      }

      if (onlyAvailable) {
        data = (data || []).filter(b => b.availableCopies > 0);
      }

      setBooks(data || []);
    } catch (err) {
      console.error('Failed to load catalog books', err);
    } finally {
      setLoading(false);
    }
  }, [searchQuery, selectedCategory, onlyAvailable]);

  useEffect(() => {
    fetchBooks();
  }, [fetchBooks]);

  const handleCardClick = (book) => {
    setSelectedBook(book);
    setIsDetailsOpen(true);
  };

  const handleCoverUpload = async (e) => {
    const file = e.target.files[0];
    if (!file) return;
    try {
      const imageUrl = await bookService.uploadCover(file);
      setFormData(prev => ({ ...prev, coverImageUrl: imageUrl }));
      setToast({ message: 'Cover image uploaded successfully!', type: 'success' });
    } catch (err) {
      setToast({ message: 'Cover image upload failed.', type: 'error' });
    }
  };

  const handleAddBookSubmit = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    try {
      await bookService.createBook(formData);
      setToast({ message: `Successfully added '${formData.title}' to catalog!`, type: 'success' });
      setIsAddModalOpen(false);
      fetchBooks();
    } catch (err) {
      setToast({ message: err.response?.data?.message || 'Failed to add book', type: 'error' });
    } finally {
      setSubmitting(false);
    }
  };

  const handleCsvSubmit = async (e) => {
    e.preventDefault();
    if (!csvFile) return;
    setSubmitting(true);
    try {
      const result = await bookService.importCSV(csvFile);
      setToast({ message: result.message || 'Bulk CSV imported successfully!', type: 'success' });
      setIsCsvModalOpen(false);
      setCsvFile(null);
      fetchBooks();
    } catch (err) {
      setToast({ message: err.response?.data?.message || 'CSV Import failed', type: 'error' });
    } finally {
      setSubmitting(false);
    }
  };

  const categories = [
    { label: 'All Categories', value: '' },
    { label: 'Computer Science', value: '1' },
    { label: 'Science Fiction', value: '2' },
    { label: 'Classics', value: '3' },
    { label: 'History', value: '4' },
    { label: 'Self-Help', value: '5' }
  ];

  return (
    <div className="space-y-6 animate-fade-in">
      {toast && <Toast {...toast} onClose={() => setToast(null)} />}

      {/* Header & Actions */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-extrabold text-slate-100 tracking-tight">Book Catalog & Inventory</h1>
          <p className="text-xs text-slate-400 mt-1">Explore titles, check physical copy availability, or manage inventory.</p>
        </div>

        {isLibrarian && (
          <div className="flex items-center gap-3">
            <button
              onClick={() => setIsCsvModalOpen(true)}
              className="px-4 py-2.5 bg-slate-800 hover:bg-slate-700 text-slate-200 border border-slate-700 rounded-xl text-xs font-semibold transition-all flex items-center gap-2"
            >
              <Upload className="w-4 h-4 text-purple-400" /> Bulk CSV Import
            </button>
            <button
              onClick={() => setIsAddModalOpen(true)}
              className="px-4 py-2.5 bg-indigo-600 hover:bg-indigo-500 text-white rounded-xl text-xs font-semibold shadow-lg shadow-indigo-600/30 transition-all flex items-center gap-2 hover:scale-[1.02]"
            >
              <PlusCircle className="w-4 h-4" /> Add New Book
            </button>
          </div>
        )}
      </div>

      {/* Search & Filter Bar */}
      <div className="glass-panel p-4 rounded-2xl border border-slate-800 space-y-4">
        <div className="flex flex-col md:flex-row gap-3">
          {/* Search Input */}
          <div className="relative flex-1">
            <Search className="w-4 h-4 text-slate-500 absolute left-3.5 top-3" />
            <input
              type="text"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              placeholder="Search by Title, Author, ISBN, or Publisher..."
              className="w-full pl-10 pr-4 py-2.5 bg-slate-900/60 border border-slate-800 rounded-xl text-sm text-slate-100 placeholder-slate-500 focus:outline-none focus:border-indigo-500 transition-colors"
            />
          </div>

          {/* Availability Checkbox Toggle */}
          <label className="flex items-center gap-2 px-4 py-2.5 bg-slate-900/60 border border-slate-800 rounded-xl text-xs text-slate-300 cursor-pointer hover:border-slate-700 transition-colors">
            <input
              type="checkbox"
              checked={onlyAvailable}
              onChange={(e) => setOnlyAvailable(e.target.checked)}
              className="rounded bg-slate-800 border-slate-700 text-indigo-600 focus:ring-0"
            />
            <span>Available Now Only</span>
          </label>
        </div>

        {/* Category Pills */}
        <div className="flex flex-wrap gap-2 pt-1 border-t border-slate-800/60">
          {categories.map((cat) => (
            <button
              key={cat.label}
              onClick={() => setSelectedCategory(cat.value)}
              className={`px-3 py-1.5 rounded-xl text-xs font-medium transition-all ${
                selectedCategory === cat.value
                  ? 'bg-indigo-600 text-white shadow-md shadow-indigo-600/30 font-semibold'
                  : 'bg-slate-900/60 hover:bg-slate-800 text-slate-400 hover:text-slate-200 border border-slate-800'
              }`}
            >
              {cat.label}
            </button>
          ))}
        </div>
      </div>

      {/* Book Grid */}
      {loading ? (
        <div className="flex items-center justify-center py-16">
          <div className="w-8 h-8 border-4 border-indigo-500/30 border-t-indigo-500 rounded-full animate-spin" />
        </div>
      ) : books.length === 0 ? (
        <div className="glass-panel p-12 text-center rounded-3xl border border-slate-800">
          <BookOpen className="w-12 h-12 text-slate-600 mx-auto mb-3" />
          <h3 className="text-base font-bold text-slate-300">No Books Found</h3>
          <p className="text-xs text-slate-500 mt-1">Try adjusting your search criteria or category filter.</p>
        </div>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
          {books.map((book) => (
            <BookCard key={book.id} book={book} onClick={() => handleCardClick(book)} />
          ))}
        </div>
      )}

      {/* Book Details Modal */}
      <BookDetailsModal
        book={selectedBook}
        isOpen={isDetailsOpen}
        onClose={() => setIsDetailsOpen(false)}
        onRefresh={fetchBooks}
      />

      {/* Add Book Modal */}
      <Modal isOpen={isAddModalOpen} onClose={() => setIsAddModalOpen(false)} title="Add New Book Title">
        <form onSubmit={handleAddBookSubmit} className="space-y-4">
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <div>
              <label className="block text-xs font-semibold text-slate-300 mb-1">Book Title *</label>
              <input
                type="text"
                required
                value={formData.title}
                onChange={(e) => setFormData({ ...formData, title: e.target.value })}
                placeholder="e.g. Clean Architecture"
                className="w-full px-3 py-2 bg-slate-900 border border-slate-800 rounded-xl text-xs text-slate-100"
              />
            </div>
            <div>
              <label className="block text-xs font-semibold text-slate-300 mb-1">ISBN *</label>
              <input
                type="text"
                required
                value={formData.isbn}
                onChange={(e) => setFormData({ ...formData, isbn: e.target.value })}
                placeholder="e.g. 978-0134494166"
                className="w-full px-3 py-2 bg-slate-900 border border-slate-800 rounded-xl text-xs text-slate-100"
              />
            </div>
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <div>
              <label className="block text-xs font-semibold text-slate-300 mb-1">Author Name *</label>
              <input
                type="text"
                required
                value={formData.authorName}
                onChange={(e) => setFormData({ ...formData, authorName: e.target.value })}
                placeholder="e.g. Robert C. Martin"
                className="w-full px-3 py-2 bg-slate-900 border border-slate-800 rounded-xl text-xs text-slate-100"
              />
            </div>
            <div>
              <label className="block text-xs font-semibold text-slate-300 mb-1">Category *</label>
              <input
                type="text"
                required
                value={formData.categoryName}
                onChange={(e) => setFormData({ ...formData, categoryName: e.target.value })}
                placeholder="e.g. Computer Science"
                className="w-full px-3 py-2 bg-slate-900 border border-slate-800 rounded-xl text-xs text-slate-100"
              />
            </div>
          </div>

          <div className="grid grid-cols-3 gap-4">
            <div>
              <label className="block text-xs font-semibold text-slate-300 mb-1">Publisher</label>
              <input
                type="text"
                value={formData.publisher}
                onChange={(e) => setFormData({ ...formData, publisher: e.target.value })}
                placeholder="Prentice Hall"
                className="w-full px-3 py-2 bg-slate-900 border border-slate-800 rounded-xl text-xs text-slate-100"
              />
            </div>
            <div>
              <label className="block text-xs font-semibold text-slate-300 mb-1">Year</label>
              <input
                type="number"
                value={formData.publishYear}
                onChange={(e) => setFormData({ ...formData, publishYear: parseInt(e.target.value) || 2024 })}
                className="w-full px-3 py-2 bg-slate-900 border border-slate-800 rounded-xl text-xs text-slate-100"
              />
            </div>
            <div>
              <label className="block text-xs font-semibold text-slate-300 mb-1">Copies Count *</label>
              <input
                type="number"
                min="1"
                required
                value={formData.totalCopies}
                onChange={(e) => setFormData({ ...formData, totalCopies: parseInt(e.target.value) || 1 })}
                className="w-full px-3 py-2 bg-slate-900 border border-slate-800 rounded-xl text-xs text-slate-100"
              />
            </div>
          </div>

          <div>
            <label className="block text-xs font-semibold text-slate-300 mb-1">Cover Image Upload</label>
            <input
              type="file"
              accept="image/*"
              onChange={handleCoverUpload}
              className="w-full text-xs text-slate-400 file:mr-3 file:py-1.5 file:px-3 file:rounded-xl file:border-0 file:text-xs file:font-semibold file:bg-indigo-600 file:text-white hover:file:bg-indigo-500"
            />
          </div>

          <div>
            <label className="block text-xs font-semibold text-slate-300 mb-1">Description</label>
            <textarea
              rows="3"
              value={formData.description}
              onChange={(e) => setFormData({ ...formData, description: e.target.value })}
              placeholder="Provide book overview..."
              className="w-full px-3 py-2 bg-slate-900 border border-slate-800 rounded-xl text-xs text-slate-100"
            />
          </div>

          <button
            type="submit"
            disabled={submitting}
            className="w-full py-2.5 bg-indigo-600 hover:bg-indigo-500 text-white font-semibold text-xs rounded-xl shadow-lg shadow-indigo-600/30 transition-all"
          >
            {submitting ? 'Saving Book...' : 'Save Book to Catalog'}
          </button>
        </form>
      </Modal>

      {/* CSV Import Modal */}
      <Modal isOpen={isCsvModalOpen} onClose={() => setIsCsvModalOpen(false)} title="Bulk Import Catalog via CSV">
        <form onSubmit={handleCsvSubmit} className="space-y-4">
          <div className="p-4 rounded-xl bg-slate-900/80 border border-slate-800 text-xs text-slate-300 space-y-2">
            <p className="font-semibold text-indigo-300">CSV Header Specification:</p>
            <code className="block bg-slate-950 p-2 rounded border border-slate-800 font-mono text-[11px] text-slate-200">
              title,isbn,author,category,publisher,year,copies
            </code>
            <p className="text-slate-400 text-[11px]">Upload your CSV file with book records to batch insert them into the catalog database.</p>
          </div>

          <div>
            <label className="block text-xs font-semibold text-slate-300 mb-1">Choose CSV File</label>
            <input
              type="file"
              accept=".csv"
              required
              onChange={(e) => setCsvFile(e.target.files[0])}
              className="w-full text-xs text-slate-400 file:mr-3 file:py-1.5 file:px-3 file:rounded-xl file:border-0 file:text-xs file:font-semibold file:bg-purple-600 file:text-white hover:file:bg-purple-500"
            />
          </div>

          <button
            type="submit"
            disabled={submitting || !csvFile}
            className="w-full py-2.5 bg-purple-600 hover:bg-purple-500 text-white font-semibold text-xs rounded-xl shadow-lg shadow-purple-600/30 transition-all"
          >
            {submitting ? 'Processing CSV Import...' : 'Import Books from CSV'}
          </button>
        </form>
      </Modal>
    </div>
  );
};

export default Catalog;
