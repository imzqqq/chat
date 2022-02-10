from pyperf import perf_counter

from chat.util.caches.lrucache import LruCache


async def main(reactor, loops):
    """
    Benchmark `loops` number of insertions into LruCache where half of them are
    evicted.
    """
    cache = LruCache(loops // 2)

    start = perf_counter()

    for i in range(loops):
        cache[i] = True

    end = perf_counter() - start

    return end
