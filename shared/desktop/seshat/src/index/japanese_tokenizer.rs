// This file was taken from https://github.com/osyoyu/tantivy-tokenizer-tiny-segmenter
//
// For some reason tantivy doesn't see that the Tokenizer trait is fully implemented and
// can't convert the tokenizer into a BoxedTokenizer.
//
// More specifically the trait BoxableTokenizer has a higher kinded trait
// bound: A: for<'a> Tokenizer<'a> + Send + Sync. Compilation fails complaining
// that the trait bound isn't satisfied while it obviously is. Just copying the
// exactly same implementation seems to satisfy the compiler.
//
// Either I'm doing something wrong or the compiler is not smart enough.

use std::iter::Enumerate;
use tantivy::tokenizer::{BoxTokenStream, Token, TokenStream, Tokenizer};

#[derive(Debug, Clone, Default)]
pub struct TinySegmenterTokenizer;

impl TinySegmenterTokenizer {
    pub fn new() -> Self {
        Default::default()
    }
}

impl Tokenizer for TinySegmenterTokenizer {
    fn token_stream<'a>(&self, text: &'a str) -> BoxTokenStream<'a> {
        TinySegmenterTokenStream::new(text).into()
    }
}

pub struct TinySegmenterTokenStream {
    tinyseg_enum: Enumerate<std::vec::IntoIter<String>>,
    current_token: Token,
    offset_from: usize,
    offset_to: usize,
}

impl TinySegmenterTokenStream {
    pub fn new(text: &str) -> TinySegmenterTokenStream {
        TinySegmenterTokenStream {
            tinyseg_enum: tinysegmenter::tokenize(text).into_iter().enumerate(),
            current_token: Token::default(),
            offset_from: 0,
            offset_to: 0,
        }
    }
}

impl TokenStream for TinySegmenterTokenStream {
    fn advance(&mut self) -> bool {
        match self.tinyseg_enum.next() {
            Some((pos, term)) => {
                self.offset_from = self.offset_to;
                self.offset_to = self.offset_from + term.len();

                let offset_from = self.offset_from;
                let offset_to = self.offset_to;

                self.current_token = Token {
                    offset_from,
                    offset_to,
                    position: pos,
                    text: term,
                    position_length: 1,
                };

                true
            }

            None => false,
        }
    }

    fn token(&self) -> &Token {
        &self.current_token
    }

    fn token_mut(&mut self) -> &mut Token {
        &mut self.current_token
    }
}
