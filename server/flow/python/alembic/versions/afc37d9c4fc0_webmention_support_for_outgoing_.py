"""Webmention support for outgoing activties

Revision ID: afc37d9c4fc0
Revises: 65387f69edfb
Create Date: 2022-07-10 14:20:46.311098

"""
import sqlalchemy as sa

from alembic import op

# revision identifiers, used by Alembic.
revision = 'afc37d9c4fc0'
down_revision = '65387f69edfb'
branch_labels = None
depends_on = None


def upgrade() -> None:
    # ### commands auto generated by Alembic - please adjust! ###
    # op.drop_column('outgoing_activity', 'webmention_target')
    # op.add_column('outgoing_activity', sa.Column('webmention_target', sa.String(), nullable=True))
    # ### end Alembic commands ###
    pass


def downgrade() -> None:
    # ### commands auto generated by Alembic - please adjust! ###
    # op.drop_column('outgoing_activity', 'webmention_target')
    # ### end Alembic commands ###
    pass
