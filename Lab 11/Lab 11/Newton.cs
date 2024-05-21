﻿namespace Lab_11
{
    public class Newton
    {
        public int n { set; get; }
        public int k { set; get; }

        public Newton(int n, int k)
        {
            this.n = n;
            this.k = k;
        }

        public double CalculateWithTasks()
        {
            if (n <= 0 || k <= 0) return -1.0;
            if (n < k) return -2.0;

            Task<double> counterTask = Task.Run(CalculateCounter);

            Task<double> denominatorTask = Task.Run(CalculateDenominator);


            counterTask.Wait();
            denominatorTask.Wait();

            return counterTask.Result / denominatorTask.Result;
        }

        public double CalculateWithDelegates()
        {
            if (n <= 0 || k <= 0) return -1.0;
            if (n < k) return -2.0;

            Func<double> counterFunc = CalculateCounter;
            Func<double> denominatorFunc = CalculateDenominator;

            var counter = counterFunc.BeginInvoke(null, null);
            var denominator = denominatorFunc.BeginInvoke(null, null);
            while (!counter.IsCompleted && !denominator.IsCompleted)
            {
            }

            return counterFunc.EndInvoke(counter) / denominatorFunc.EndInvoke(denominator);
        }

        public async Task<double> CalculateWithAsyncAwait()
        {
            if (n <= 0 || k <= 0) return -1.0;
            if (n < k) return -2.0;

            double counter = await Task.Run(CalculateCounter);
            double denominator = await Task.Run(CalculateDenominator);

            return counter / denominator;
        }

        public double CalculateCounter()
        {
            double ret = 1;
            for (int i = n - k + 1; i <= n; i++)
            {
                ret *= i;
            }
            return ret;
        }

        public double CalculateDenominator()
        {
            double ret = 1;
            for (int i = 1; i <= k; i++)
            {
                ret *= i;
            }
            return ret;
        }
    }
}
