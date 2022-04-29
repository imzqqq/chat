const chai = require('chai');
const matrixUtils = require('../src/matrixUtils');

require('../src/logger');

const expect = chai.expect;

describe('matrixUtils', function() {
    describe('parseHostnameAndPort', function() {
        it('returns correct results', async () => {
            expect(matrixUtils.parseHostnameAndPort('chat.imzqqq.top')).to.deep.equal({
                hostname: 'chat.imzqqq.top',
                port: '8880',
                defaultPort: true,
            });
            expect(matrixUtils.parseHostnameAndPort('chat.imzqqq.top:1234')).to.deep.equal({
                hostname: 'chat.imzqqq.top',
                port: '1234',
                defaultPort: false,
            });
        });
    });

    describe('validateDomain', function() {
        it('returns correct results', async () => {
            expect(matrixUtils.validateDomain('chat.imzqqq.top')).to.be.true;
            expect(matrixUtils.validateDomain('matrix.domain.tld')).to.be.true;

            expect(matrixUtils.validateDomain('1.2.3.4')).to.be.false;
            expect(matrixUtils.validateDomain('1234:5678::abcd')).to.be.false;
            expect(matrixUtils.validateDomain('matrix')).to.be.false;
            expect(matrixUtils.validateDomain('matrix org')).to.be.false;
            expect(matrixUtils.validateDomain('42')).to.be.false;
        });
    });
});
